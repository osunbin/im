package com.bin.im.server.event.model;

public class SysOfflineMsg {
    private long toUid;
    private long fromUid;
    private String msgContent;
    private int fromSource;
    private int toSource;
    private long productId;

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getFromSource() {
        return fromSource;
    }

    public void setFromSource(int fromSource) {
        this.fromSource = fromSource;
    }

    public int getToSource() {
        return toSource;
    }

    public void setToSource(int toSource) {
        this.toSource = toSource;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}