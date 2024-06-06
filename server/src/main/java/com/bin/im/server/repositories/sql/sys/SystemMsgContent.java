package com.bin.im.server.repositories.sql.sys;

public class SystemMsgContent {

    private long msgId;

    private String msgContent;



    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }


}
