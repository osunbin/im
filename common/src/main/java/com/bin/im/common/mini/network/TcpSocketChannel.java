package com.bin.im.common.mini.network;

import com.bin.im.common.internal.concurrent.MPSCQueue;
import com.bin.im.common.mini.buffer.NioBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.bin.im.common.mini.buffer.NioBufferPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpSocketChannel implements AsyncChannel{

    private static final Logger logger = LoggerFactory.getLogger(TcpSocketChannel.class);

    private final ByteBuffer bufferLen = ByteBuffer.allocate(4);

    private final SocketChannel channel;
    private final EventLoopGroup eventLoopGroup;

    private final EventLoop writeWorker;
    private SelectionKey writeSelectionKey;

    private SocketAddress remote;
    private MPSCQueue<ByteBuffer> writeBuffers = new MPSCQueue<>();

    private byte ops = 0;

    private int readTimeout = 0;
    private int writeTimeout = 0;

    private List<ByteBuffer> combineBuffer = new ArrayList<>(8);

    private AtomicBoolean finishConnect = new AtomicBoolean(false);

    public TcpSocketChannel(EventLoopGroup group) throws IOException {
        this(group, SocketChannel.open(), SocketSettings.create(), false);
    }

    public TcpSocketChannel(EventLoopGroup group, SocketSettings socketSettings) throws IOException {
        this(group, SocketChannel.open(), socketSettings, false);
    }

    public TcpSocketChannel(EventLoopGroup group, SocketChannel socketChannel, SocketSettings socketSettings) throws IOException {
        this(group, socketChannel, socketSettings, false);
    }

    public TcpSocketChannel(EventLoopGroup eventLoopGroup, SocketChannel socketChannel, SocketSettings socketSettings, boolean read) throws IOException {
        channel = socketChannel;
        this.eventLoopGroup = eventLoopGroup;
        writeWorker = eventLoopGroup.getWriteWorker();
        writeBuffers.setConsumerThread(writeWorker.getWorkerThread());
        channel.configureBlocking(false);

        if (read) {
            ops = SelectionKey.OP_READ;
        }
        wrapChannel(socketSettings);
    }


    private void wrapChannel(SocketSettings socketSettings) throws IOException {
        if (socketSettings == null) return;
        socketSettings.applySettings(channel);
        if (socketSettings.hasImplReadTimeout()) {
            this.readTimeout = socketSettings.getImplReadTimeoutMillis();
        }
        if (socketSettings.hasImplWriteTimeout()) {
            this.writeTimeout = socketSettings.getImplWriteTimeoutMillis();
        }
    }

    public static void main(String[] args) throws IOException {
        TcpSocketChannel tcpSocketChannel = new TcpSocketChannel(null);
        tcpSocketChannel.connect(new InetSocketAddress("localhost", 9922));
        tcpSocketChannel.write(null);
    }


    public SocketAddress getRemoteAddress() {
        return channel.socket().getRemoteSocketAddress();
    }

    public SocketAddress getLocalAddress() throws IOException {
        return channel.getLocalAddress();
    }

    @Override
    public void connect(SocketAddress remote) throws IOException {
        if (channel.isConnected()) {
            throw new AlreadyConnectedException();
        }
        this.remote = remote;
        channel.connect(remote);
        finishConnect();
    }


    public void finishConnect() {
        if (finishConnect.compareAndSet(false, true)) {
            writeWorker.addRegister(new Consumer<Selector>() {
                @Override
                public void accept(Selector selector) {
                    try {
                        writeSelectionKey = channel.register
                                (selector, SelectionKey.OP_CONNECT, TcpSocketChannel.this);
                    } catch (ClosedChannelException e) {
                        close();
                    }
                }
            });
        }
    }


    public void onConnect() {
        SocketChannel channel = (SocketChannel) writeSelectionKey.channel();
        boolean connected;
        try {
            connected = channel.finishConnect();
        } catch (IOException e) {
            close();
            return;
        }

        if (connected) {
            writeWorker.addRegister(new Consumer<Selector>() {
                @Override
                public void accept(Selector selector) {
                    try {
                        writeSelectionKey = channel.register(selector, ops,
                                TcpSocketChannel.this);
                    } catch (ClosedChannelException e) {
                        close();
                    }
                }
            });
        } else {
            logger.info("Connection key was received but the channel was not connected - this is not possible without some bug in Java NIO");
            close();
        }
    }

    @Override
    public void write(ByteBuffer src) {
        writeBuffers.offer(src);
        if (ops >= 0) {
            updateInterests();
        }
    }


    public void onWrite() {

        ops = (byte) (ops | 0x80); // 取消 事件

        if (writeBuffers.size() > 0) {
            for (int i = 0; i < 5; i++) {
                ByteBuffer buffer = writeBuffers.poll();
                if (buffer != null)
                    combineBuffer.add(buffer);
            }
            ByteBuffer[] byteBuffers = combineBuffer.toArray(new ByteBuffer[0]);
            try {
                channel.write(byteBuffers);
                combineBuffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 回收 TODO
        }
        ops = (byte) (ops & 0x7f); // 修复

        updateInterests();

    }


    public void onRead() {
        ops = (byte) (ops | 0x80);

        try {
            channel.read(bufferLen);
            int length = bufferLen.getInt();
            int totalLen = length + 4;
            NioBuffer nioBuffer = NioBufferPool.allocate(totalLen);
            nioBuffer.writeInt(length);

            ByteBuffer buffer = nioBuffer.writableBuffer();
            channel.read(buffer);

            int realLen = buffer.position();
            if (realLen == totalLen) {
                nioBuffer.skipWritableBytes(realLen);

                eventLoopGroup.onMessage(nioBuffer);
            } else {
                logger.info("totalLen  not eq realLen discard");
            }
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }


        ops = (byte) (ops & 0x7f);
        updateInterests();
    }


    private void updateInterests() {
        if (ops < 0) return;
        byte newOps = (byte) (writeBuffers.size() == 0 ? 0 : SelectionKey.OP_WRITE);
        if (ops != newOps) {
            ops = newOps;
            writeSelectionKey.interestOps(ops);
            writeWorker.wakeup();
        }
    }


    public void close() {
        if (channel == null || !channel.isOpen()) return;
        if (writeSelectionKey != null) {
            writeSelectionKey.cancel();
        }
        try {
            channel.close();
        } catch (IOException e) {
            logger.warn("Failed to close channel {}", channel, e);
        }
    }


}

