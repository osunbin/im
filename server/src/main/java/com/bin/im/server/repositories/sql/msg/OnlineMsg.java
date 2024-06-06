package com.bin.im.server.repositories.sql.msg;

public class OnlineMsg {
    /**
     *  消息 id
     */
    private long msgId;

    private long toUid;

    private long fromUid;

    /**
     *  消息去重
     */
    private long clientMsgId;
    /**
     *  0 - 正向
     *  1 - 反向
     *
     */
    private int direction;

    private int msgType;


    private String msgContent;

    /**
     *  sendTime  发消息时间/消息生成的时间
     */
    private long timestamp;

    /**
     *  删除、撤回、正常
     */
    private int msgFlag;



    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public int getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(int msgFlag) {
        this.msgFlag = msgFlag;
    }

    public boolean effective() {
        if (msgFlag == 0) {
            return true;
        }
        return false;
    }

    public boolean forward() {
        if (direction == 0)
            return true;
        return false;
    }

}
