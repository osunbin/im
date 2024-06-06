package com.bin.im.server.repositories.model.cloud;

import com.bin.im.server.repositories.model.BaseMsgModel;

public class TOBOnlineMsgExtModel extends BaseMsgModel {

    private long msgId;

    private long uidC;

    private int msgType;

    private int fromUid;

    private int status;

    private int nbuyer;

    private long timestamp;

    private long clientMsgId;

    private long productId;



    private String  msgContent;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getUidC() {
        return uidC;
    }

    public void setUidC(long uidC) {
        this.uidC = uidC;
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



    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
