package com.bin.im.server.domain;

/**
 *  resp
 *          uid
 *          username  //用户名：主要匿名时使用
 *          session   //第一次登陆返回
 *          errmsg     //错误信息
 */
public class UserLogModel {

    private long uid;
    private int sourceType;
    private String userToken;	//token 验证安全
    private String clientVersion; 	//客户端版本号
    private String  deviceId; 	//设备id
    private String clientIp;
    private String username;
    private String passwd;
    private String session; //用于匿名验证

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

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getSession() {
        return session;
    }
}
