package com.bin.im.server.domain;


import static com.bin.im.server.repositories.ImDas.onlineMsgSize;

public class ContactPreloadSysMsg {
    private long contactUid;       //对方uid
    private int msgCount;
    private long lastMsgId;


    public long getContactUid() {
        return contactUid;
    }

    public void setContactUid(long contactUid) {
        this.contactUid = contactUid;
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
