package com.bin.im.server.event.model;


import java.util.List;

public class PersonalMsg {
    private String msg;
    private int sourceType;
    private List<Long> fromUids;
    private long toUid;
    private int msgPid;
    private long msgId;
    private int ptype;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
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

    public int getMsgPid() {
        return msgPid;
    }

    public void setMsgPid(int msgPid) {
        this.msgPid = msgPid;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }


    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

}