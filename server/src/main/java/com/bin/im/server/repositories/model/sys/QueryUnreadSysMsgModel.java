package com.bin.im.server.repositories.model.sys;

import com.bin.im.server.repositories.model.BaseMsgModel;

public class QueryUnreadSysMsgModel extends BaseMsgModel {
    private int msgType;

    private long lastMsgId;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(long lastMsgId) {
        this.lastMsgId = lastMsgId;
    }
}
