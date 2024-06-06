package com.bin.im.common.mini.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static java.net.StandardSocketOptions.*;

public final class SocketSettings {
    private static final byte DEF_BOOL = -1;
    private static final byte TRUE = 1;
    private static final byte FALSE = 0;

    private final byte keepAlive;
    private final byte reuseAddress;
    private final byte tcpNoDelay;

    private final int sendBufferSize;
    private final int receiveBufferSize;
    private final int implReadTimeout;
    private final int implWriteTimeout;
    private final int lingerTimeout;

    // region builders
    private SocketSettings(int sendBufferSize, int receiveBufferSize, byte keepAlive, byte reuseAddress, byte tcpNoDelay, int implReadTimeout, int implWriteTimeout, int lingerTimeout) {
        this.sendBufferSize = sendBufferSize;
        this.receiveBufferSize = receiveBufferSize;
        this.keepAlive = keepAlive;
        this.reuseAddress = reuseAddress;
        this.tcpNoDelay = tcpNoDelay;
        this.implReadTimeout = implReadTimeout;
        this.implWriteTimeout = implWriteTimeout;
        this.lingerTimeout = lingerTimeout;
    }

    public static SocketSettings create() {
        return new SocketSettings(0, 0, DEF_BOOL, DEF_BOOL, DEF_BOOL, 0, 0,  -1);
    }

    /**
     * Creates a default socket settings with socket option {@code TCP_NODELAY} enabled.
     * Enabling this option turns off Nagle's algorithm.
     * Note, that by default, operating system has {@code TCP_NODELAY} option disabled.
     *
     * @return default socket settings
     */
    public static SocketSettings createDefault() {
        return new SocketSettings(0, 0, DEF_BOOL, DEF_BOOL, TRUE, 0, 0,  -1);
    }

    public SocketSettings withSendBufferSize(int sendBufferSize) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress, tcpNoDelay, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }

    public SocketSettings withReceiveBufferSize(int receiveBufferSize) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress, tcpNoDelay, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }

    public SocketSettings withKeepAlive(boolean keepAlive) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive ? TRUE : FALSE, reuseAddress, tcpNoDelay, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }

    public SocketSettings withReuseAddress(boolean reuseAddress) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress ? TRUE : FALSE, tcpNoDelay, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }

    public SocketSettings withTcpNoDelay(boolean tcpNoDelay) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress, tcpNoDelay ? TRUE : FALSE, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }

    public SocketSettings withImplReadTimeout(int implReadTimeout) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress, tcpNoDelay, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }

    public SocketSettings withImplWriteTimeout(int implWriteTimeout) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress, tcpNoDelay, implReadTimeout, implWriteTimeout,  lingerTimeout);
    }


    public SocketSettings withLingerTimeout(int lingerTimeout) {
        return new SocketSettings(sendBufferSize, receiveBufferSize, keepAlive, reuseAddress, tcpNoDelay, implReadTimeout, implWriteTimeout,  (lingerTimeout / 1000));
    }

    // endregion

    public void applySettings(SocketChannel channel) throws IOException {
        if (sendBufferSize != 0) {
            channel.setOption(SO_SNDBUF, sendBufferSize);
        }
        if (receiveBufferSize != 0) {
            channel.setOption(SO_RCVBUF, receiveBufferSize);
        }
        if (keepAlive != DEF_BOOL) {
            channel.setOption(SO_KEEPALIVE, keepAlive != FALSE);
        }
        if (reuseAddress != DEF_BOOL) {
            channel.setOption(SO_REUSEADDR, reuseAddress != FALSE);
        }
        if (tcpNoDelay != DEF_BOOL) {
            channel.setOption(TCP_NODELAY, tcpNoDelay != FALSE);
        }
        if (lingerTimeout != -1) {
            channel.setOption(SO_LINGER, lingerTimeout);
        }
    }

    public boolean hasSendBufferSize() {
        return sendBufferSize != 0;
    }


    public int getSendBufferSizeBytes() {
        return sendBufferSize;
    }

    public boolean hasReceiveBufferSize() {
        return receiveBufferSize != 0;
    }


    public int getReceiveBufferSizeBytes() {
        return receiveBufferSize;
    }

    public boolean hasKeepAlive() {
        return keepAlive != DEF_BOOL;
    }

    public boolean getKeepAlive() {
        return keepAlive != FALSE;
    }

    public boolean hasReuseAddress() {
        return reuseAddress != DEF_BOOL;
    }

    public boolean getReuseAddress() {
        return reuseAddress != FALSE;
    }

    public boolean hasTcpNoDelay() {
        return tcpNoDelay != DEF_BOOL;
    }

    public boolean getTcpNoDelay() {
        return tcpNoDelay != FALSE;
    }

    public boolean hasImplReadTimeout() {
        return implReadTimeout != 0;
    }


    public int getImplReadTimeoutMillis() {
        return implReadTimeout;
    }

    public boolean hasImplWriteTimeout() {
        return implWriteTimeout != 0;
    }


    public int getImplWriteTimeoutMillis() {
        return implWriteTimeout;
    }


    public boolean hasLingerTimeout() {
        return lingerTimeout != -1;
    }


}
