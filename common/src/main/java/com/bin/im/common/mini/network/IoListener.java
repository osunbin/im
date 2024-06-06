package com.bin.im.common.mini.network;

import com.bin.im.common.mini.buffer.NioBuffer;

public interface IoListener {


    void onMessage(NioBuffer nioBuffer);
}
