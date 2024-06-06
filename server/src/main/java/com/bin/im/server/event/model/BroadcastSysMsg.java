package com.bin.im.server.event.model;

import java.util.List;

public class BroadcastSysMsg{
    private long msgId;
    private List<Long> fromUids;
    private long toUid;
    private int toSource;
    private int ptype;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public List<Long> getFromUids() {
        return fromUids;
    }

    public void setFromUids(List<Long> fromUids) {
        this.fromUids = fromUids;
    }

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public int getToSource() {
        return toSource;
    }

    public void setToSource(int toSource) {
        this.toSource = toSource;
    }


    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }
}