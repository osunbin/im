package com.bin.im.entry.http.session;

public class RelayP2PMsg {
    private long fromUid;
    private long toUid;
    private int sourceType;
    private int msgFlag;
    private int fromSourceType;
    private int msgType;
    private String msgContent;

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public int getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(int msgFlag) {
        this.msgFlag = msgFlag;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getFromSourceType() {
        return fromSourceType;
    }

    public void setFromSourceType(int fromSourceType) {
        this.fromSourceType = fromSourceType;
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
}
