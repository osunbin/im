package com.bin.im.server.event.model;

public class EventSysMsg {

    private int msgType;
    private String msgContent;
  //  private int sourceType;
    private long fromUid;
    private int toSource;
    private long toUid;
    private int msgPid;
    private long msgId;
    private int ptype = 1;   //无该字段或ptype=1 ，表示存离线； ptype=0，表示不存离线


    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }



    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public int getToSource() {
        return toSource;
    }

    public void setToSource(int toSource) {
        this.toSource = toSource;
    }

    public long getToUid() {
        return toUid;
    }

    public void setToUid(long toUid) {
        this.toUid = toUid;
    }

    public int getMsgPid() {
        return msgPid;
    }

    public void setMsgPid(int msgPid) {
        this.msgPid = msgPid;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }
}
