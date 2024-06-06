package com.bin.im.server.repositories.model.sys;

import com.bin.im.server.repositories.model.BaseMsgModel;


public class PersonSysMsgIdModel extends BaseMsgModel {


    private int msgType;

    private long startMsgId;

    private int count;



    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
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


}
