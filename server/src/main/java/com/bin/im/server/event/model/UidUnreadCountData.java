package com.bin.im.server.event.model;

import com.bin.im.server.domain.ContactUnreadCountInfo;

import java.util.List;

public class UidUnreadCountData {
    private long uid;
    private int sourceType;
    private int msgType;
    private List<ContactUnreadCountInfo> unreadCounts;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public List<ContactUnreadCountInfo> getUnreadCounts() {
        return unreadCounts;
    }

    public void setUnreadCounts(List<ContactUnreadCountInfo> unreadCounts) {
        this.unreadCounts = unreadCounts;
    }
}
