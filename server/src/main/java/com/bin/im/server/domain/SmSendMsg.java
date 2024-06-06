package com.bin.im.server.domain;

public class SmSendMsg {
    private long reqUid;
    private int sourceType;
    private long userUid;     //用户uid
    private long muid;        //母uid
    private long suid;       //子uid
    //私信方向标识，true表示user_uid是私信发送者，false表示user_uid是私信接收者
    private boolean fromUser;

    private long clientMsgId;
    private String msgContent;
    private int msgType;           //业务层面消息类型
    private String noticeMsg;  //前端提示语
    private long infoId;
    public long getReqUid() {
        return reqUid;
    }

    public void setReqUid(long reqUid) {
        this.reqUid = reqUid;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

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

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }
}
