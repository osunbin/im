package com.bin.im.server.domain;

import java.util.List;

public class BatchMsgAck {


    private long toUid;

    private List<MsgAck> msgAcks;



    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }


    public List<MsgAck> getMsgAcks() {
        return msgAcks;
    }

    public void setMsgAcks(List<MsgAck> msgAcks) {
        this.msgAcks = msgAcks;
    }
}
