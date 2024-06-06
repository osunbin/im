package com.bin.im.server.repositories.sql.sys;

public class SystemBroadCastMsg {

    /**
     *  消息 id
     */
    private long msgId;
    /**
     *  系统联系人
     */
    private long suid;


    /**
     *  发送时间
     */
    private long sendTime;

    /**
     *  消息有效时间 - 开始时间
     */
    private long beginTs;

    /**
     *  消息有效时间 - 结束时间
     */
    private long endTs;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getSuid() {
        return suid;
    }

    public void setSuid(long suid) {
        this.suid = suid;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }


    public long getBeginTs() {
        return beginTs;
    }

    public void setBeginTs(long beginTs) {
        this.beginTs = beginTs;
    }

    public long getEndTs() {
        return endTs;
    }

    public void setEndTs(long endTs) {
        this.endTs = endTs;
    }
}
