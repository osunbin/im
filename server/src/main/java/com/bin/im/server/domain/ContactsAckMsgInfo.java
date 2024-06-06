package com.bin.im.server.domain;

public class ContactsAckMsgInfo {
    private long contactUid;
    private long lastMsgId;

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
}
