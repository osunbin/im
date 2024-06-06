package com.bin.im.server.repositories.model;



public class BaseMsgModel {

    private long dstUid;
    private long srcUid;

    public BaseMsgModel() {
    }

    public BaseMsgModel(long dstUid, long srcUid) {
        this.dstUid = dstUid;
        this.srcUid = srcUid;
    }

    public long getDstUid() {
        return dstUid;
    }

    public void setDstUid(long dstUid) {
        this.dstUid = dstUid;
    }

    public long getSrcUid() {
        return srcUid;
    }

    public void setSrcUid(long srcUid) {
        this.srcUid = srcUid;
    }



}
