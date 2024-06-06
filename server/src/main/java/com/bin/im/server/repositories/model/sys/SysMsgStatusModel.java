package com.bin.im.server.repositories.model.sys;

import java.util.List;

public class SysMsgStatusModel {
    private long dstUid;
    private int status;
    private List<Long> msgIds;


    public long getDstUid() {
        return dstUid;
    }

    public void setDstUid(long dstUid) {
        this.dstUid = dstUid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Long> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(List<Long> msgIds) {
        this.msgIds = msgIds;
    }
}
