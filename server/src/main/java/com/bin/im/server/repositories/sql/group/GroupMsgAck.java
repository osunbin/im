package com.bin.im.server.repositories.sql.group;

public class GroupMsgAck {
    /**
     *  自增主键/雪花算法
     */
    private long id;

    /**
     *  群
     */
    private long gid;
    /**
     *  发送
     */
    private long suid;
    /**
     *  接收
     */
    private long recvUid;
    /**
     *  消息id
     */
    private long msgId;
    /**
     *  是否已读
     */
    private boolean isAck;


    private long readTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getRecvUid() {
        return recvUid;
    }

    public void setRecvUid(long recvUid) {
        this.recvUid = recvUid;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public boolean isAck() {
        return isAck;
    }

    public void setAck(boolean ack) {
        isAck = ack;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
