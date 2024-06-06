package com.bin.im.server.repositories.model.contacts;

public class UserContactModel {
    private long uidA;

    private long uidB;
    private long clientMsgId;

    private long timestamp;
    private int unreadCount;

    private int msgDirection;

    private int msgType;

    private String msgContent;

    private boolean isDel;

    private long readTime;

    private long unreadMinTimestamp;

    private long uidC;

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

    public int getMsgDirection() {
        return msgDirection;
    }



    public void setMsgDirection(int msgDirection) {
        this.msgDirection = msgDirection;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }



    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getUnreadMinTimestamp() {
        return unreadMinTimestamp;
    }

    public void setUnreadMinTimestamp(long unreadMinTimestamp) {
        this.unreadMinTimestamp = unreadMinTimestamp;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public long getUidC() {
        return uidC;
    }

    public void setUidC(long uidC) {
        this.uidC = uidC;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserContactModel{");
        sb.append("uidA=").append(uidA);
        sb.append(", uidB=").append(uidB);
        sb.append(", clientMsgId=").append(clientMsgId);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", unreadCount=").append(unreadCount);
        sb.append(", msgDirection=").append(msgDirection);
        sb.append(", msgType=").append(msgType);
        sb.append(", msgContent='").append(msgContent).append('\'');
        sb.append(", isDel=").append(isDel);
        sb.append(", readTime=").append(readTime);
        sb.append(", unreadMinTimestamp=").append(unreadMinTimestamp);
        sb.append(", uidC=").append(uidC);
        sb.append('}');
        return sb.toString();
    }
}
