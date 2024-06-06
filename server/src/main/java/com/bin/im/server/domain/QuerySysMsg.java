package com.bin.im.server.domain;


import static com.bin.im.server.repositories.ImDas.onlineMsgSize;

public class QuerySysMsg {
    private long reqUid;
    private long sysContactId;
    private int sourceType;
    private int msgCount;
    private long lastMsgId;


    public long getReqUid() {
        return reqUid;
    }

    public void setReqUid(long reqUid) {
        this.reqUid = reqUid;
    }

    public long getSysContactId() {
        return sysContactId;
    }

    public void setSysContactId(long sysContactId) {
        this.sysContactId = sysContactId;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getMsgCount() {
        if (msgCount == 0) return onlineMsgSize;
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }


    public long getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(long lastMsgId) {
        this.lastMsgId = lastMsgId;
    }
}
