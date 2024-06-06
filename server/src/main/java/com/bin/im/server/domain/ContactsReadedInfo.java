package com.bin.im.server.domain;

public class ContactsReadedInfo {
    private long contactUid;
    private long readTime;

    public long getContactUid() {
        return contactUid;
    }

    public void setContactUid(long contactUid) {
        this.contactUid = contactUid;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
