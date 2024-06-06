package com.bin.im.server.repositories.model.cloud;

public class QueryOfflineCountModel {
    private long dstUid;
    private int fromUid;
    private int status;
    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
