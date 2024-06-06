package com.bin.im.server.domain;

import java.util.HashMap;
import java.util.Map;

public class MsgReadedNotify {
    private long fromUid;
    private long readTime;

    public MsgReadedNotify(long fromUid, long readTime) {
        this.fromUid = fromUid;
        this.readTime = readTime;
    }

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public Map<String,Object> toMap() {
        Map<String,Object> json = new HashMap<>();
        json.put("fromUid",fromUid);
        json.put("readTime",readTime);
        return json;
    }
}
