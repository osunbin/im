package com.bin.im.entry.tcp.encrypt;

public interface Encrypt {


    /**
     *  加密
     */
    byte[] encrypt(byte[] content);

    /**
     *  解密
     */
    byte[] decrypt(byte[] content);
}
