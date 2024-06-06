package com.bin.im.server.repositories.model.cloud;

import com.bin.im.server.repositories.model.BaseMsgModel;

public class CloudMsgIdModel extends BaseMsgModel {

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
