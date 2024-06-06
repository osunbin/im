package com.bin.im.common.mini.network;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public interface AsyncChannel {

    void connect(SocketAddress remote) throws IOException;

    void write(ByteBuffer src);
}
