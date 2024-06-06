package com.bin.im.server.domain;

import java.util.List;

public class MsgAck {

    private long fromUid;



    private List<Long> msgIds;


    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public List<Long> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(List<Long> msgIds) {
        this.msgIds = msgIds;
    }
}
