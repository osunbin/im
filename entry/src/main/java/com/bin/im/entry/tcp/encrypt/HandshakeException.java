package com.bin.im.entry.tcp.encrypt;

public class HandshakeException extends RuntimeException{

    private int code;

    public HandshakeException(String message, int code) {
        super(message);
        this.code = code;
    }

    public HandshakeException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }



    public int getCode() {
        return code;
    }
}
