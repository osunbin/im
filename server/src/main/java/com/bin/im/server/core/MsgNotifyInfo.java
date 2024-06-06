package com.bin.im.server.core;

public class MsgNotifyInfo {
    private long uid;
    private String esIp;
    private int esPort;

    public static MsgNotifyInfo create(long uid, String esIp, int esPort) {
        return new MsgNotifyInfo(uid,esIp,esPort);
    }


    public MsgNotifyInfo(long uid, String esIp, int esPort) {
        this.uid = uid;
        this.esIp = esIp;
        this.esPort = esPort;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getEsIp() {
        return esIp;
    }

    public void setEsIp(String esIp) {
        this.esIp = esIp;
    }

    public int getEsPort() {
        return esPort;
    }

    public void setEsPort(int esPort) {
        this.esPort = esPort;
    }
}
