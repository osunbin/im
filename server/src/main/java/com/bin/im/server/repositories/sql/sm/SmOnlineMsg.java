package com.bin.im.server.repositories.sql.sm;

public class SmOnlineMsg {
    /**
     *  消息 id
     */
    private long msgId;

    private long toUid;

    private long fromUid;

    /**
     *  字母号
     */
    private long smUid;

    /**
     *  消息去重
     */
    private long clientMsgId;
    /**
     *  uidA 给 uidB 发消息
     *  0 - 正向  a -> b
     *  1 - 反向  b -> a
     */
    private int direction;

    private boolean uidSm;

    private int msgType;


    private String msgContent;

    /**
     *  sendTime  发消息时间/消息生成的时间
     */
    private long timestamp;


    private int delFlag;

    /**
     *  商品 id
     */
    private long infoId;


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


    public long getSmUid() {
        return smUid;
    }

    public void setSmUid(long smUid) {
        this.smUid = smUid;
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

    public boolean isUidSm() {
        return uidSm;
    }

    public void setUidSm(boolean uidSm) {
        this.uidSm = uidSm;
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

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }
}
