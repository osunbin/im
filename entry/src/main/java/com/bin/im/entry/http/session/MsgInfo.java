package com.bin.im.entry.http.session;

public class MsgInfo {
    private long uid;
    private long msgId;
    private String jsonMsg;
    private RelayP2PMsg relayMsg;
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getJsonMsg() {
        return jsonMsg;
    }

    public void setJsonMsg(String jsonMsg) {
        this.jsonMsg = jsonMsg;
    }


    public RelayP2PMsg getRelayMsg() {
        return relayMsg;
    }

    public void setRelayMsg(RelayP2PMsg relayMsg) {
        this.relayMsg = relayMsg;
    }
}
