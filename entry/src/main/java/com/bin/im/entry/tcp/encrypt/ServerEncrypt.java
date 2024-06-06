package com.bin.im.entry.tcp.encrypt;

public interface ServerEncrypt extends Encrypt{

    byte[] initial();

    byte[] clientHello(byte[] pk);

    void verify(byte[] aes);
}
