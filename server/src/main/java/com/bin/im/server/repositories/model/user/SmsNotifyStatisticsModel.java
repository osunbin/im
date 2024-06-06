package com.bin.im.server.repositories.model.user;

public class SmsNotifyStatisticsModel {
    private long  uid;
    private int smsDayBitmap;
    private long timestamp;
    private int status;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSmsDayBitmap() {
        return smsDayBitmap;
    }

    public void setSmsDayBitmap(int smsDayBitmap) {
        this.smsDayBitmap = smsDayBitmap;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
