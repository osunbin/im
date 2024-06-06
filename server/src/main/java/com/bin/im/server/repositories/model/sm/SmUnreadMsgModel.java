package com.bin.im.server.repositories.model.sm;

/**
 *  查询未读消息数量
 */
public class SmUnreadMsgModel extends BaseSmCloudMsgModel {
    private long msgId;
    private int msgDirection;


    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(int msgDirection) {
        this.msgDirection = msgDirection;
    }
}
