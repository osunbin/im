package com.bin.im.server.repositories.model.sm;


public class SmContactsModel {
    private long uidA;
    private long uidB;
    private long uidC;
    private boolean uidSm;
    private int msgType;
    private int msgDirection;
    private long clientMsgId;
    private long timestamp;
    private String msgContent;
    private int unreadCount;
    private boolean isDel;

    private long readTime;

    public long getUidA() {
        return uidA;
    }

    public void setUidA(long uidA) {
        this.uidA = uidA;
    }

    public long getUidB() {
        return uidB;
    }

    public void setUidB(long uidB) {
        this.uidB = uidB;
    }

    public long getUidC() {
        return uidC;
    }

    public void setUidC(long uidC) {
        this.uidC = uidC;
    }

    public boolean isUidSm() {
        return uidSm;
    }

    public void setUidSm(boolean uidSm) {
        this.uidSm = uidSm;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(int msgDirection) {
        this.msgDirection = msgDirection;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
