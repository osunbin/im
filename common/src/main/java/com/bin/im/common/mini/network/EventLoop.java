package com.bin.im.common.mini.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class EventLoop implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(EventLoop.class);

    private volatile boolean running = true;


    private final Selector selector;

    private int idleInterval = 1000;

    private final ConcurrentLinkedQueue<Consumer<Selector>> registers = new ConcurrentLinkedQueue<Consumer<Selector>>();

    private Thread workerThread;

    EventLoop(Selector selector) {
        this.selector = selector;
    }

    void addRegister(Consumer<Selector> register) {
        registers.offer(register);
        selector.wakeup();
    }

    void wakeup() {
        selector.wakeup();
    }


    public void close() {
        running = false;
        try {
            selector.close();
        } catch (IOException e) {
        }
    }

    public Thread getWorkerThread() {
        return workerThread;
    }

    @Override
    public void run() {
        workerThread = Thread.currentThread();

        Set<SelectionKey> keySet = selector.selectedKeys();

        int loop = 0;

        while (running) {
            Consumer<Selector> register;
            while ((register = registers.poll()) != null) {
                register.accept(selector);
            }
            try {
                if (!keySet.isEmpty()) {
                    if (loop++ > 16) {
                        loop = 0;
                        selector.selectNow();
                    }
                } else if (selector.select() == 0) {
                    continue;
                } else {
                    loop = 0;
                }
            } catch (IOException e) {
                logger.warn("IO Error in {}", selector, e);
            }

            keySet = selector.selectedKeys();
            processSelectedKeys(keySet);
        }

    }

    private void processSelectedKeys(Set<SelectionKey> keySet) {
        Iterator<SelectionKey> keyIterator = keySet.iterator();
        // 执行本次已触发待处理的事件
        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();
            keyIterator.remove();
            if (!key.isValid()) {

                continue;
            }
            if (key.isAcceptable()) {

                TcpServerSocketChannel serverSocketChannel =
                        (TcpServerSocketChannel) key.attachment();
                serverSocketChannel.onAccept();
                continue;
            }


            TcpSocketChannel tcpSocketChannel =
                    (TcpSocketChannel) key.attachment();
            if (key.isReadable()) {
                tcpSocketChannel.onRead();
            } else if (key.isWritable()) {
                removeOps(key, SelectionKey.OP_WRITE);
                tcpSocketChannel.onWrite();
            } else if (key.isConnectable()) {
                tcpSocketChannel.onConnect();
            } else {
                throw new IllegalStateException("illegal state");
            }

        }
    }

    public void removeOps(SelectionKey selectionKey, int opt) {
        if (selectionKey.isValid() && (selectionKey.interestOps() & opt) != 0) {
            selectionKey.interestOps(selectionKey.interestOps() & ~opt);
        }
    }
}
