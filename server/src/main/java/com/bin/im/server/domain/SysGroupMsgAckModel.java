package com.bin.im.server.domain;

public class SysGroupMsgAckModel {
    private long msgTimestamp;
    private long msgId;


    public SysGroupMsgAckModel() {
        msgTimestamp = 0;
        msgId = 0;
    }

    public SysGroupMsgAckModel(long msgTimestamp, long msgId) {
        this.msgTimestamp = msgTimestamp;
        this.msgId = msgId;
    }

    public long getMsgTimestamp() {
        return msgTimestamp;
    }

    public long getMsgId() {
        return msgId;
    }
}
