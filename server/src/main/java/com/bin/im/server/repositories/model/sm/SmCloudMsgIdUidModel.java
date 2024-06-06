package com.bin.im.server.repositories.model.sm;

public class SmCloudMsgIdUidModel extends BaseSmCloudMsgModel {
    private long uidC;
    private long clientMsgId;

    private String msgContent;
    public long getUidC() {
        return uidC;
    }

    public void setUidC(long uidC) {
        this.uidC = uidC;
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
}
