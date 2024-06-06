package com.bin.im.server.domain;

import com.bin.im.common.json.JsonHelper;

public class LoginInfo {
    private long uid;
    private long loginTime;  //登录时间
    private String loginESIp;   //entry ip
    private int loginESPort;  //entry 端口

    private int cliType;      //客户端类型

    private int keepaliveTime; //心跳

    private String version;  //版本号   "103.5.3"  pb版本

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginESIp() {
        return loginESIp;
    }

    public void setLoginESIp(String loginESIp) {
        this.loginESIp = loginESIp;
    }

    public int getLoginESPort() {
        return loginESPort;
    }

    public void setLoginESPort(int loginESPort) {
        this.loginESPort = loginESPort;
    }

    public int getCliType() {
        return cliType;
    }

    public void setCliType(int cliType) {
        this.cliType = cliType;
    }

    public int getKeepaliveTime() {
        return keepaliveTime;
    }

    public void setKeepaliveTime(int keepaliveTime) {
        this.keepaliveTime = keepaliveTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String toJson() {
       return JsonHelper.toJson(this);
    }


    public boolean isLogined(LoginInfo oldLoginInfo) {

        if (oldLoginInfo != null &&
            !"".equals(oldLoginInfo.getLoginESIp()) &&         // 用户已登陆
            (!loginESIp.equals(oldLoginInfo.getLoginESIp()) || // 不同的entry
           (loginESIp.equals(oldLoginInfo.getLoginESIp()) &&
           loginESPort != oldLoginInfo.getLoginESPort()))) {  //同一个entry不同的port
            return true;
        }
        return false;
    }
}
