package com.bin.im.server.repositories.model.contacts;

import java.util.List;

public class QueryMsgModel {
    private long fuUid;
    private long startTimestamp;
    private long endTimestamp;
    private int offset;
    private int limit;
    private List<Long> duUid;


    public long getFuUid() {
        return fuUid;
    }

    public void setFuUid(long fuUid) {
        this.fuUid = fuUid;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Long> getDuUid() {
        return duUid;
    }

    public void setDuUid(List<Long> duUid) {
        this.duUid = duUid;
    }
}
