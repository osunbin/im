package com.bin.im.server.repositories.model.contacts;

import java.util.List;

public class QueryUserContactModel {
    private long uidA;
    private boolean isDel;
    private List<Integer> msgTypes;
    private int pageNum;
    private int pageSize;

    public long getUidA() {
        return uidA;
    }

    public void setUidA(long uidA) {
        this.uidA = uidA;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public List<Integer> getMsgTypes() {
        return msgTypes;
    }

    public void setMsgTypes(List<Integer> msgTypes) {
        this.msgTypes = msgTypes;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
