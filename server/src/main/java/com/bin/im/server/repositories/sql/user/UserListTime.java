package com.bin.im.server.repositories.sql.user;

public class UserListTime {
    private long uid;
    /**
     *  用户好友列表时间戳
     *  加入或删除修改
     */
    private long contactsTime;
    /**
     *  加群 或 退出群 的时间
     */
    private long groupTime;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getContactsTime() {
        return contactsTime;
    }

    public void setContactsTime(long contactsTime) {


        this.contactsTime = contactsTime;
    }

    public long getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(long groupTime) {
        this.groupTime = groupTime;
    }
}
