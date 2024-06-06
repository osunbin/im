package com.bin.im.server.common.type;

public enum ServiceType {

    NORMAL(1),
    DUBBO(2),
    EVENT(3);

    private int code;

    ServiceType(int code) {
        this.code =code;
    }

    public int getCode() {
        return code;
    }

    public  boolean needInterfaces() {
        if (this == EVENT)  return true;
        return false;
    }

}
