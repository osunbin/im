package com.bin.im.server.event.model;

public class LoginChange {
    private long uid;
    private int sourceType;
    private int loginFlag;
    private String deviceId;
    private String clientVersion;
    private String clientIp;
    private long time;



    public static LoginChange ofLogin(long uid, int source,String deviceId, String clientVersion, String clientIp,long time) {
        return new LoginChange(uid,source,1,deviceId,clientVersion,clientIp,time);
    }

    public static LoginChange ofLogout(long uid, int source, long time) {
        return new LoginChange(uid,source,-1,time);
    }

    public LoginChange(long uid, int source, int loginFlag, long time) {
        this.uid = uid;
        this.sourceType = source;
        this.loginFlag = loginFlag;
        this.time = time;
    }

    public LoginChange(long uid, int source, int loginFlag, String deviceId, String clientVersion, String clientIp,long time) {
        this.uid = uid;
        this.sourceType = source;
        this.loginFlag = loginFlag;
        this.deviceId = deviceId;
        this.clientVersion = clientVersion;
        this.clientIp = clientIp;
        this.time = time;
    }


    public long getUid() {
        return uid;
    }

    public int getSourceType() {
        return sourceType;
    }

    public int getLoginFlag() {
        return loginFlag;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getClientIp() {
        return clientIp;
    }

    public boolean isLogin() {
        return loginFlag == 1;
    }

    public long getTime() {
        return time;
    }
}
