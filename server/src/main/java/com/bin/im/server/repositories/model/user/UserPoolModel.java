package com.bin.im.server.repositories.model.user;

public class UserPoolModel {
    private long uid;
    private long fromUid;


    private long expireTimestamp;

    private long timestamp;

    private int status;

    private int followWechat;

    private int isSeller;

    private int openPushAuth;

    private String mobile;

    private int clientType;

    private int fromClientType;

    private long productId;

    private String productName;

    private String fromUserName;

    private String msgContent;

    private int msgType;
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public long getExpireTimestamp() {
        return expireTimestamp;
    }

    public void setExpireTimestamp(long expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFollowWechat() {
        return followWechat;
    }

    public void setFollowWechat(int followWechat) {
        this.followWechat = followWechat;
    }

    public int getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(int isSeller) {
        this.isSeller = isSeller;
    }

    public int getOpenPushAuth() {
        return openPushAuth;
    }

    public void setOpenPushAuth(int openPushAuth) {
        this.openPushAuth = openPushAuth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public int getFromClientType() {
        return fromClientType;
    }

    public void setFromClientType(int fromClientType) {
        this.fromClientType = fromClientType;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
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
}
