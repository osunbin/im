package com.bin.im.server.repositories.sql.sm;

public class UserSmContacts {
    private long uidA;

    private long uidB;


    private long uidC;
    /**
     *  0 A add B
     *  1 B add A
     */
    private int direction;

    private int uidSm;

    /**
     *  未读消息数量
     */
    private int unreadCount;

    /**
     * 在线 最后一次 ack msgId
     */
    private long lastAckMsgId;

    /**
     *  最后一次在线 读取消息时间
     */
    private long readTime;
    /**
     *  最后一次聊天的时间(包括离线)
     */
    private long timestamp;

    /**
     *  加好友时间
     */
    private long createTime;
    /**
     *  联系人删除标志
     */
    private int contactsFlag;


    public long getUidA() {
        return uidA;
    }

    public void setUidA(long uidA) {
        this.uidA = uidA;
    }

    public long getUidB() {
        return uidB;
    }

    public void setUidB(long uidB) {
        this.uidB = uidB;
    }

    public long getUidC() {
        return uidC;
    }

    public void setUidC(long uidC) {
        this.uidC = uidC;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getUidSm() {
        return uidSm;
    }

    public void setUidSm(int uidSm) {
        this.uidSm = uidSm;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getLastAckMsgId() {
        return lastAckMsgId;
    }

    public void setLastAckMsgId(long lastAckMsgId) {
        this.lastAckMsgId = lastAckMsgId;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getContactsFlag() {
        return contactsFlag;
    }

    public void setContactsFlag(int contactsFlag) {
        this.contactsFlag = contactsFlag;
    }
}
