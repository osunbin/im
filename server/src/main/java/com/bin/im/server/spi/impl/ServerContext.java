package com.bin.im.server.spi.impl;

import com.bin.im.common.id.self.IdGenerate;

public class ServerContext {
    private long uid;
    private String esIp;
    private int esPort;
    private int sourceType;
    private long logId;
    private String logPre;
    private String clientIp;
    private long seq;
    private long datacenterId = -1; // 数据中心标识
    private long machineId =  -1;    // 机器标识

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

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getLogPre() {
        return logPre;
    }

    public void setLogPre(String logPre) {
        this.logPre = logPre;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }



    public  long generateMsgId() {
       return IdGenerate.generateMsgId(datacenterId,machineId);
    }
}
