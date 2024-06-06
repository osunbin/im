package com.bin.im.server.domain;

import java.util.List;

public class MsgReaded {
    private long reqUid;
    private List<Long> toUids;

    public long getReqUid() {
        return reqUid;
    }

    public void setReqUid(long reqUid) {
        this.reqUid = reqUid;
    }

    public List<Long> getToUids() {
        return toUids;
    }

    public void setToUids(List<Long> toUids) {
        this.toUids = toUids;
    }
}
