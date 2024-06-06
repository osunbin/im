package com.bin.im.server.domain;

public class SysGroupMsg {
    private long sysUid;
    private long toUid;
    private int sourceType;
    private String msgContent;

    public long getSysUid() {
        return sysUid;
    }

    public void setSysUid(long sysUid) {
        this.sysUid = sysUid;
    }

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
