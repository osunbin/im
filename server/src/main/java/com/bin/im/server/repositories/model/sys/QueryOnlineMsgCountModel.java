package com.bin.im.server.repositories.model.sys;

import com.bin.im.server.repositories.model.BaseMsgModel;

public class QueryOnlineMsgCountModel extends BaseMsgModel {
    private int msgType;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
