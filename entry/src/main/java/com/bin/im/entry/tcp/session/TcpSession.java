package com.bin.im.entry.tcp.session;


import com.bin.im.entry.tcp.encrypt.ServerEncrypt;
import io.netty.channel.Channel;

public class TcpSession {

    public static final int UserKeepAliveThreshold = 300 * 1000;

    private long uid;
    private Channel channel;
    private ServerEncrypt serverEncrypt;
    private int sourceType;
    private long loginTime;
    private long sessionTimeout;
    private long expireTime;
    private long lastUserKeepAliveSendTime = System.currentTimeMillis();
    private boolean loginReady = true;
    private boolean isAuth;

    public void noLoginReady() {
        loginReady = false;
    }


    public boolean isLoginReady() {
        return loginReady;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    public ServerEncrypt getServerEncrypt() {
        return serverEncrypt;
    }

    public void setServerEncrypt(ServerEncrypt serverEncrypt) {
        this.serverEncrypt = serverEncrypt;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        expireTime = System.currentTimeMillis() + sessionTimeout;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public void updateExpireTime() {

        this.expireTime = System.currentTimeMillis() + sessionTimeout;
    }

    public void resetExpireTime() {
        expireTime = 0;
    }

    public void setLoginReady(boolean loginReady) {
        this.loginReady = loginReady;
    }

    public long getLastUserKeepAliveSendTime() {
        return lastUserKeepAliveSendTime;
    }

    public void setLastUserKeepAliveSendTime(long lastUserKeepAliveSendTime) {
        this.lastUserKeepAliveSendTime = lastUserKeepAliveSendTime;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }


    public boolean isIgnore(long currTime) {
        if (currTime < getLastUserKeepAliveSendTime() + UserKeepAliveThreshold) {
            return true;
        }
        return false;
    }
}
