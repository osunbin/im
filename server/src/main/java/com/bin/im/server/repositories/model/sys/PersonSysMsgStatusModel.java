package com.bin.im.server.repositories.model.sys;

import com.bin.im.server.repositories.model.BaseMsgModel;

import java.util.List;

public class PersonSysMsgStatusModel extends BaseMsgModel {


    private int msgType;



    private int count;

    private List<Long> msgids;
    private long startTime;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}

