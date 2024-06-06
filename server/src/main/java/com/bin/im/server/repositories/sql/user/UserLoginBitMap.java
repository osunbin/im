package com.bin.im.server.repositories.sql.user;

public class UserLoginBitMap {

    private long uid;
    private long loginBitMap;
    private int loginDay;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getLoginBitMap() {
        return loginBitMap;
    }

    public void setLoginBitMap(long loginBitMap) {
        this.loginBitMap = loginBitMap;
    }

    public int getLoginDay() {
        return loginDay;
    }

    public void setLoginDay(int loginDay) {
        this.loginDay = loginDay;
    }
}
