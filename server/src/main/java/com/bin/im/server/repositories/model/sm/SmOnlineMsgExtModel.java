package com.bin.im.server.repositories.model.sm;

public class SmOnlineMsgExtModel {

    private long msgId;

    private long clientMsgId;

    private long uidA;
    private long uidB;
    private long uidC;

    private boolean uidSm;
    private boolean msgDirection;

    private int msgType;

    private long timestamp;



    private long infoId;

    private String msgContent;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

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

    public boolean isMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(boolean msgDirection) {
        this.msgDirection = msgDirection;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
