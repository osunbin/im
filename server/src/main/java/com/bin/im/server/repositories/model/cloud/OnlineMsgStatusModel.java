package com.bin.im.server.repositories.model.cloud;

import java.util.List;

public class OnlineMsgStatusModel {
    private long uid;
    private int status;

    private List<Long> uidList;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Long> getUidList() {
        return uidList;
    }

    public void setUidList(List<Long> uidList) {
        this.uidList = uidList;
    }
}
