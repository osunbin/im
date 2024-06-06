package com.bin.im.server.repositories.model.sm;

public class SmCloudMsgRangeTimeModel extends BaseSmCloudMsgModel {
    private long startTimestamp;
    private long endTimestamp;

    private int count;


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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
