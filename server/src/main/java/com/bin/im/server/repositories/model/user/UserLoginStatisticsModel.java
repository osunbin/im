package com.bin.im.server.repositories.model.user;

public class UserLoginStatisticsModel {
    private long uid;
    private int sourceType;
    private long timestamp;
    private int loginDayBitmap;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLoginDayBitmap() {
        return loginDayBitmap;
    }

    public void setLoginDayBitmap(int loginDayBitmap) {
        this.loginDayBitmap = loginDayBitmap;
    }
}
