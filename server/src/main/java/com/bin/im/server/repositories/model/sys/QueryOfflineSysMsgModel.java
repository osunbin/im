package com.bin.im.server.repositories.model.sys;

import java.util.List;

public class QueryOfflineSysMsgModel {
    private long dstUid;
    private long startTime;
    private long startMsgId;
    private int count;
    private List<Long> srcUids;


    public long getDstUid() {
        return dstUid;
    }

    public void setDstUid(long dstUid) {
        this.dstUid = dstUid;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartMsgId() {
        return startMsgId;
    }

    public void setStartMsgId(long startMsgId) {
        this.startMsgId = startMsgId;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Long> getSrcUids() {
        return srcUids;
    }

    public void setSrcUids(List<Long> srcUids) {
        this.srcUids = srcUids;
    }
}
