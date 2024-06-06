package com.bin.im.server.domain;

public class SMSendMsgNotify {

    private long userUid;  //用户uid
    private long muid;     //母uid
    private long suid;      //子uid
    private boolean fromUser;  //私信方向标识，1表示userUid是私信发送者，0表示userUid是私信接收者
    private long msgTime;  //服务端生成的私信发送的毫秒级时间戳
    private long clientMsgId;    //发送客户端生成的在客户端的全局范围内唯一标识的私信id
    private String msgContent;  //消息的内容
    private int msgType;        //业务层面消息类型
    private String noticeMsg;   // 前端提示语
    private long msgId;        // 私信消息的服务端id



    public long getUserUid() {
        return userUid;
    }

    public void setUserUid(long userUid) {
        this.userUid = userUid;
    }

    public long getMuid() {
        return muid;
    }

    public void setMuid(long muid) {
        this.muid = muid;
    }

    public long getSuid() {
        return suid;
    }

    public void setSuid(long suid) {
        this.suid = suid;
    }


    public boolean isFromUser() {
        return fromUser;
    }

    public void setFromUser(boolean fromUser) {
        this.fromUser = fromUser;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getNoticeMsg() {
        return noticeMsg;
    }

    public void setNoticeMsg(String noticeMsg) {
        this.noticeMsg = noticeMsg;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }
}
