package com.bin.im.server.domain;

public class ContactUnreadCountInfo {
    private long contactUid;

    private int msgCount;

    public ContactUnreadCountInfo(long contactUid, int msgCount) {
        this.contactUid = contactUid;
        this.msgCount = msgCount;
    }

    public long getContactUid() {
        return contactUid;
    }

    public void setContactUid(long contactUid) {
        this.contactUid = contactUid;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }
}
