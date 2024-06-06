package com.bin.im.server.domain;

public class SmSendMsgAckModel {
    private long msgTime;
    private String message;
    private long msgId;
    private long sUid;

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getsUid() {
        return sUid;
    }

    public void setsUid(long sUid) {
        this.sUid = sUid;
    }
}
