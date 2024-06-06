package com.bin.im.server.repositories.model.contacts;

import java.util.List;

public class QueryContactsModel {
    private long uidA;
    private long uidB;
    private long timestamp;
    private boolean isDel;
    private List<Integer> msgTypeList;
    private int count;
    public long getUidA() {
        return uidA;
    }

    public void setUidA(long uidA) {
        this.uidA = uidA;
    }

    public long getUidB() {
        return uidB;
    }

    public void setUidB(long uidB) {
        this.uidB = uidB;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsDel() {
        return isDel;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }

    public List<Integer> getMsgTypeList() {
        return msgTypeList;
    }

    public void setMsgTypeList(List<Integer> msgTypeList) {
        this.msgTypeList = msgTypeList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
