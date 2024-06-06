package com.bin.im.server.domain;

import com.bin.im.common.json.JsonHelper;


public class MsgAckInfo {
    private long fromUid;
    private long toUid;
    private int msgType;
    private long clientMsgId;
    private long msgId;
    private int sourceType;
    private int timeout;
    private long timestamp;
    private String msgContent;

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }


    public String getAckKey() {

        StringBuilder sb = new StringBuilder();
        sb.append("{")
        .append('"').append("fromUid").append('"').append(":").append(fromUid).append(",")
        .append('"').append("toUid").append('"').append(":").append(toUid).append(",")
        .append('"').append("msgId").append('"').append(":").append(msgId)
        .append("}");
        return sb.toString();
    }



    public static MsgAckInfo parseFromString(String json) {
       return JsonHelper.fromJson(json,MsgAckInfo.class);
    }

    public String getJson() {

       return JsonHelper.toJson(this);
    }
}
