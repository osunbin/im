package com.bin.im.server.repositories.model.sys;

import com.bin.im.server.repositories.model.BaseMsgModel;

public class DeleteOnePersonSysMsgModel extends BaseMsgModel {
    private long msgId;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

}
