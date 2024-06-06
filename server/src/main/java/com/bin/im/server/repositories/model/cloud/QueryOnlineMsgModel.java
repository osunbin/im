package com.bin.im.server.repositories.model.cloud;

import com.bin.im.server.repositories.model.BaseMsgModel;

import java.util.List;

public class QueryOnlineMsgModel extends BaseMsgModel {

    private int msgType;

    private long startTime;
    private long startMsgId;
    private int count;
    private List<Long> msgids;


    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartMsgId() {
        return startMsgId;
    }

    public void setStartMsgId(long startMsgId) {
        this.startMsgId = startMsgId;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Long> getMsgids() {
        return msgids;
    }

    public void setMsgids(List<Long> msgids) {
        this.msgids = msgids;
    }
}
