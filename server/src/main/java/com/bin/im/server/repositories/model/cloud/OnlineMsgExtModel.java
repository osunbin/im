package com.bin.im.server.repositories.model.cloud;

import com.bin.im.server.repositories.model.BaseMsgModel;

public class OnlineMsgExtModel extends BaseMsgModel {
    private long msgId;
    private int msgType;
    private int fromUid;
    private int status;
    private int nbuyer;
    private int reserved;
    private long timestamp;
    private long clientMsgId;
    private long productId;
    private long reserved1;

    private String msgContent;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNbuyer() {
        return nbuyer;
    }

    public void setNbuyer(int nbuyer) {
        this.nbuyer = nbuyer;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getReserved1() {
        return reserved1;
    }

    public void setReserved1(long reserved1) {
        this.reserved1 = reserved1;
    }


    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
