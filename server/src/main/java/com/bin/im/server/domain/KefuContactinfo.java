package com.bin.im.server.domain;

public class KefuContactinfo {
    private long uidB;
    private long timestamp;

    public KefuContactinfo(long uidB, long timestamp) {
        this.uidB = uidB;
        this.timestamp = timestamp;
    }

    public long getUidB() {
        return uidB;
    }

    public void setUidB(long uidB) {
        this.uidB = uidB;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
