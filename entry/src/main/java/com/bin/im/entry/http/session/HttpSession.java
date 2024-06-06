package com.bin.im.entry.http.session;

import com.bin.im.common.internal.utils.TimeUtils;

public class HttpSession {
    private long uid;
    // last active time
    private long lastTime;
    private long loginTime;
    /**
     *  if user finishes login(login/login_ready)
     *  0 => not yet
     *  1 => yes
     */
    private int login;

    private String clientIp;

    private int sourceType;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public void refresh() {
        lastTime = TimeUtils.currTimeMillis();
    }

}
