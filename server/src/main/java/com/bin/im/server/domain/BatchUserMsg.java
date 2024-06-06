package com.bin.im.server.domain;

import java.util.List;

public class BatchUserMsg {
    private long fromUid;
    private List<ContactsLastAckMsg> contactsLastAckMsgs;
    private int status = 0; // 0 第一次拉取  1 - 持续拉取   一直拉取 直到返回结果 0 就不再继续拉取
    public BatchUserMsg(long fromUid, List<ContactsLastAckMsg> contactsLastAckMsgs) {
        this.fromUid = fromUid;
        this.contactsLastAckMsgs = contactsLastAckMsgs;
    }

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public List<ContactsLastAckMsg> getContactsLastAckMsgs() {
        return contactsLastAckMsgs;
    }

    public void setContactsLastAckMsgs(List<ContactsLastAckMsg> contactsLastAckMsgs) {
        this.contactsLastAckMsgs = contactsLastAckMsgs;
    }

    public void initStatus() {
        status = 0;
    }

    public boolean first() {
        if (status == 0) return true;
        return false;
    }

}
