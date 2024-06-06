package com.bin.im.server.domain;

import java.util.HashMap;
import java.util.Map;

public class BackwardMsgNotify {

    private long fromUid;
    private long toUid;
    private long clientMsgId;

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


    public long getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(long clientMsgId) {
        this.clientMsgId = clientMsgId;
    }


    public Map<String,Object> toMap() {
        Map<String,Object> json = new HashMap<>();
        json.put("fromUid",fromUid);
        json.put("toUid",toUid);
        json.put("clientMsgId",clientMsgId);
        return json;
    }
}
