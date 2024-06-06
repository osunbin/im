package com.bin.im.server.repositories.model.sm;

public class SmCloudMsgTimeModel extends BaseSmCloudMsgModel {
    private long timestamp;
    private int count;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
