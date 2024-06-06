package com.bin.im.server.domain;


import static com.bin.im.server.repositories.ImDas.onlineMsgSize;

public class ContactsLastAckMsg {
    private long contactUid;
    private long lastMsgId;
    private int count;

    public ContactsLastAckMsg(long contactUid, long lastMsgId) {
        this.contactUid = contactUid;
        this.lastMsgId = lastMsgId;
    }

    public long getContactUid() {
        return contactUid;
    }

    public void setContactUid(long contactUid) {
        this.contactUid = contactUid;
    }

    public long getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(long lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public int getCount() {
        if (count == 0) return onlineMsgSize;
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
