package com.bin.im.entry.tcp.encrypt;

public class QuickConnectRequest {
    private long uid;
    private int sourceType;
    private byte[] encryptKey;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public byte[] getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(byte[] encryptKey) {
        this.encryptKey = encryptKey;
    }
}
