package com.bin.im.entry.tcp.encrypt;

public interface ClientEncrypt extends Encrypt{

    byte[] serverHello(byte[] pk);

    void finalHello(byte[] aesKey);

    byte[] quickConnect();
}
