package com.bin.im.server.domain;

import java.util.List;

public class UserUnreadMsg {
    private long fromUid;
    private List<Long> toUids;

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public List<Long> getToUids() {
        return toUids;
    }

    public void setToUids(List<Long> toUids) {
        this.toUids = toUids;
    }
}
