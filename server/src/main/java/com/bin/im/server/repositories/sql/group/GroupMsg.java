package com.bin.im.server.repositories.sql.group;

/**
 *  primary key - gid + msgId
 *  sharding - gid
 */
public class GroupMsg {
    /**
     *  群
     */
    private long gid;
    /**
     *  谁发的消息
     */
    private long suid;
    /**
     *  消息 id
     */
    private long msgId;

    /**
     *  客户端生成唯一 ID 去重
     */
    private long clientMsgId;

    /**
     *  消息类型
     */
    private int msgType;
    /**
     *  发送时间
     */
    private long sendTime;
    /**
     *  消息内容
     */
    private String msgContent;


    private int msgFlag;

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getSuid() {
        return suid;
    }

    public void setSuid(long suid) {
        this.suid = suid;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }


    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {

        this.msgContent = msgContent;
    }

    public int getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(int msgFlag) {
        this.msgFlag = msgFlag;
    }
}
