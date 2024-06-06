package com.bin.im.entry.model;

import io.netty.channel.Channel;

public class SocketUserLoginReq extends UserLoginReq {

    private UserLoginReq userLoginReq;

    private Channel loginChannel;


    public SocketUserLoginReq(UserLoginReq userLoginReq) {
        this.userLoginReq = userLoginReq;
    }

    public Channel getLoginChannel() {
        return this.loginChannel;
    }

    public void setLoginChannel(Channel loginChannel) {
        this.loginChannel = loginChannel;
    }


    @Override
    public byte[] getPbData(int logId, int seq) {
        return userLoginReq.getPbData(logId, seq);
    }

    @Override
    public byte[] getLoginReadyPbData(int logId, int seq) {
        return userLoginReq.getLoginReadyPbData(logId, seq);
    }

    @Override
    public String toString() {
        return userLoginReq.toString();
    }

    @Override
    public int getT() {
        return userLoginReq.getT();
    }

    @Override
    public void setT(int t) {
        userLoginReq.setT(t);
    }

    @Override
    public long getUid() {
        return userLoginReq.getUid();
    }

    @Override
    public void setUid(long uid) {
        userLoginReq.setUid(uid);
    }

    @Override
    public String getPpu() {
        return userLoginReq.getPpu();
    }

    @Override
    public void setPpu(String ppu) {
        userLoginReq.setPpu(ppu);
    }

    @Override
    public String getPcCode() {
        return userLoginReq.getPcCode();
    }

    @Override
    public void setPcCode(String pcCode) {
        userLoginReq.setPcCode(pcCode);
    }

    @Override
    public String getClientVersion() {
        return userLoginReq.getClientVersion();
    }

    @Override
    public void setClientVersion(String clientVersion) {
        userLoginReq.setClientVersion(clientVersion);
    }

    @Override
    public String getToken() {
        return userLoginReq.getToken();
    }

    @Override
    public void setToken(String token) {
        userLoginReq.setToken(token);
    }

    @Override
    public int getSource() {
        return userLoginReq.getSource();
    }

    @Override
    public void setSource(int source) {
        userLoginReq.setSource(source);
    }

    @Override
    public long getClientIp() {
        return userLoginReq.getClientIp();
    }

    @Override
    public void setClientIp(long clientIp) {
        userLoginReq.setClientIp(clientIp);
    }

    @Override
    public int getLoginState() {
        return userLoginReq.getLoginState();
    }

    @Override
    public void setLoginState(int loginState) {
        userLoginReq.setLoginState(loginState);
    }

    @Override
    public void setLoginingState() {
        userLoginReq.setLoginingState();
    }

    @Override
    public void setLoginedState() {
        userLoginReq.setLoginedState();
    }

    @Override
    public void setOutLoginState() {
        userLoginReq.setOutLoginState();
    }
}
