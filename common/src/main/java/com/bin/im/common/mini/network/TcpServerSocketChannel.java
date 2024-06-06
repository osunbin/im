package com.bin.im.common.mini.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TcpServerSocketChannel {

    private static final Logger logger = LoggerFactory.getLogger(TcpServerSocketChannel.class);


    private final ServerSocketChannel serverSocketChannel;

    private final EventLoopGroup eventLoopGroup;
    private SelectionKey selectionKey;

    private final EventLoop acceptWorker;

    private SocketSettings socketSettings;

    private List<TcpSocketChannel> childSockets = new ArrayList<>();

    public TcpServerSocketChannel(EventLoopGroup eventLoopGroup) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        this.eventLoopGroup = eventLoopGroup;
        acceptWorker = eventLoopGroup.getAcceptWorker();
    }

    public static void main(String[] args) throws IOException {
        TcpServerSocketChannel tcpServerSocketChannel = new TcpServerSocketChannel(null);
        tcpServerSocketChannel.listen(new InetSocketAddress("localhost", 9066));

    }

    public void listen(InetSocketAddress address) throws IOException {
        listen(address, ServerSocketSettings.create(), SocketSettings.create());
    }

    public void listen(InetSocketAddress address, ServerSocketSettings serverSocketSettings,
                       SocketSettings socketSettings) throws IOException {
        serverSocketSettings.applySettings(serverSocketChannel);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(address, serverSocketSettings.getBacklog());
        acceptWorker.addRegister(new Consumer<Selector>() {
            @Override
            public void accept(Selector selector) {
                try {
                    selectionKey = serverSocketChannel.register(selector,
                            SelectionKey.OP_ACCEPT, TcpServerSocketChannel.this);
                } catch (ClosedChannelException e) {
                    close();
                }
            }
        });
        this.socketSettings = socketSettings;
    }

    public void onAccept() {
        for (; ; ) {
            SocketChannel socketChannel = null;
            TcpSocketChannel tcpSocketChannel = null;
            try {
                socketChannel = serverSocketChannel.accept();
                if (socketChannel == null)
                    break;

                tcpSocketChannel =
                        new TcpSocketChannel(eventLoopGroup, socketChannel,
                                socketSettings, true);
                tcpSocketChannel.finishConnect();

                childSockets.add(tcpSocketChannel);

                acceptWorker.removeOps(selectionKey, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                if (tcpSocketChannel != null)
                    tcpSocketChannel.close();
            }
        }
    }


    public void close() {
        if (serverSocketChannel == null || !serverSocketChannel.isOpen()) return;
        if (selectionKey != null) {
            selectionKey.cancel();
        }
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            logger.warn("Failed to close channel {}", serverSocketChannel, e);
        }
    }

}
