package com.bin.im.server.event.model;

public class OnlineMsgData {
    private long toUidMsgId;
    private long fromUidMsgId;
    private long clientMsgId;
    private long toUid;
    private long fromUid;
    private int msgType;
    private long msgSendTime;
    private String msgContent;
    private long infoId;
    private int toUserIsOnline;
    private int reqUserType;
    private int fromSource;
    private String noticeMsg;
    private String version;

    public long getToUidMsgId() {
        return toUidMsgId;
    }

    public void setToUidMsgId(long toUidMsgId) {
        this.toUidMsgId = toUidMsgId;
    }

    public long getFromUidMsgId() {
        return fromUidMsgId;
    }

    public void setFromUidMsgId(long fromUidMsgId) {
        this.fromUidMsgId = fromUidMsgId;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
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

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getMsgSendTime() {
        return msgSendTime;
    }

    public void setMsgSendTime(long msgSendTime) {
        this.msgSendTime = msgSendTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public int getToUserIsOnline() {
        return toUserIsOnline;
    }

    public void setToUserIsOnline(int toUserIsOnline) {
        this.toUserIsOnline = toUserIsOnline;
    }

    public int getReqUserType() {
        return reqUserType;
    }

    public void setReqUserType(int reqUserType) {
        this.reqUserType = reqUserType;
    }

    public int getFromSource() {
        return fromSource;
    }

    public void setFromSource(int fromSource) {
        this.fromSource = fromSource;
    }

    public String getNoticeMsg() {
        return noticeMsg;
    }

    public void setNoticeMsg(String noticeMsg) {
        this.noticeMsg = noticeMsg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
