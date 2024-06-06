package com.bin.im.server.event.model;

public class KeepAliveSysMsg {
   private long toUid;
   private int sourceType;

    public KeepAliveSysMsg(long toUid, int sourceType) {
        this.toUid = toUid;
        this.sourceType = sourceType;
    }


    public long getToUid() {
        return toUid;
    }

    public int getSourceType() {
        return sourceType;
    }
}
