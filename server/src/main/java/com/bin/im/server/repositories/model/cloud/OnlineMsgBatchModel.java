package com.bin.im.server.repositories.model.cloud;

import java.util.List;

public class OnlineMsgBatchModel {
    private long msgId;

    private long dstUid;

    private int fromUid;

    private int status;
    private int count;
    private List<Long> srcUids;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }


    public long getDstUid() {
        return dstUid;
    }

    public void setDstUid(long dstUid) {
        this.dstUid = dstUid;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
