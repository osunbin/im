package com.bin.im.server.repositories.model.sm;

public class SmCloudMsgIdModel extends BaseSmCloudMsgModel {

    private long clientMsgId;

    private String msgContent;

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

