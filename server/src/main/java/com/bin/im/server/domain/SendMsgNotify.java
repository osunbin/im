package com.bin.im.server.domain;

import java.util.HashMap;
import java.util.Map;

public class SendMsgNotify {
    private long fromUid;
    private long toUid;
    private long time;
    private long clientMsgId;
    private String msgContent;
    private int msgType;
    private int reqUserType;
    private long msgId;
    private String noticeMsg;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public int getReqUserType() {
        return reqUserType;
    }

    public void setReqUserType(int reqUserType) {
        this.reqUserType = reqUserType;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getNoticeMsg() {
        return noticeMsg;
    }

    public void setNoticeMsg(String noticeMsg) {
        this.noticeMsg = noticeMsg;
    }


    public Map<String,Object> toMap() {
        Map<String,Object> json = new HashMap<>();
        json.put("module","msg");
        json.put("cmd","notify");
        json.put("fromUid",fromUid);
        json.put("toUid",toUid);
        json.put("msgId",msgId);
        json.put("msgType",msgType);
        json.put("msgContent",msgContent);
        json.put("time",time);
        json.put("clientMsgId",clientMsgId);
        json.put("noticeMsg",noticeMsg);
        return json;
    }
}
