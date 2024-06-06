package com.bin.im.server.repositories.model.contacts;

public class ContactListModel {
    private long uid;
    private long uidB;
    private int msgType;
    private boolean direct;
    private boolean msgRecvOnline;
    private short reserved;
    private int contactStyle;
    private long timestamp;
    private long clientMsgId;
    private String data;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUidB() {
        return uidB;
    }

    public void setUidB(long uidB) {
        this.uidB = uidB;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public boolean isMsgRecvOnline() {
        return msgRecvOnline;
    }

    public void setMsgRecvOnline(boolean msgRecvOnline) {
        this.msgRecvOnline = msgRecvOnline;
    }

    public short getReserved() {
        return reserved;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }

    public int getContactStyle() {
        return contactStyle;
    }

    public void setContactStyle(int contactStyle) {
        this.contactStyle = contactStyle;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
