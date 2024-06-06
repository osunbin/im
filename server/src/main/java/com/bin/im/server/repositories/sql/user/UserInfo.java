package com.bin.im.server.repositories.sql.user;

public class UserInfo {
    private long uid;

    /**
     *  类似微信号
     */
    private String identity;
    /**
     *  用户昵称
     */
    private String nickName;
    /**
     *  账号
     */
    private String account;
    /**
     *  密码
     */
    private String password;


    /**
     *  1 - 男
     *  2 - 女
     */
    private int gender;

    /**
     *  用户 名片  json
     */
    private String ucard;

    /**
     * 用户信息的开发程度,那些人能查看我的信息-仅限好友、群组、禁止所有人、限制特定的人
     */
    private int openLevel;

    /**
     *  加好友认证-需要验证、不许允许别人添加
     */
    private int contactsLevel;

    /**
     *  消息接收策略-允许所有人给我发消息,允许群成员发点对点信息，只有好友才能发点对点信息
     */
    private int msgPolicy;

    /**
     *  生日
     */
    private int birthday = 20200617;
    /**
     *  创建时间
     */
    private long createTime;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUcard() {
        return ucard;
    }

    public void setUcard(String ucard) {
        this.ucard = ucard;
    }

    public int getOpenLevel() {
        return openLevel;
    }

    public void setOpenLevel(int openLevel) {
        this.openLevel = openLevel;
    }

    public int getContactsLevel() {
        return contactsLevel;
    }

    public void setContactsLevel(int contactsLevel) {
        this.contactsLevel = contactsLevel;
    }

    public int getMsgPolicy() {
        return msgPolicy;
    }

    public void setMsgPolicy(int msgPolicy) {
        this.msgPolicy = msgPolicy;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
