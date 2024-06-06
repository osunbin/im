package com.bin.im.server.repositories.sql.contacts;

public class UserContacts {

    private long uidA;

    private long uidB;


    /**
     *  0 A add B
     *  1 B add A
     */
    private int direction;

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
     *  发消息/接收消息时间
     *
     */
    private long lastTime;

    /**
     *  普通
     *  卖家
     *  系统
     *
     */
    private int contactsType;
    /**
     *  加好友时间
     */
    private long addTime;
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


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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


    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }


    public int getContactsType() {
        return contactsType;
    }

    public void setContactsType(int contactsType) {
        this.contactsType = contactsType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getContactsFlag() {
        return contactsFlag;
    }

    public void setContactsFlag(int contactsFlag) {
        this.contactsFlag = contactsFlag;
    }

}
