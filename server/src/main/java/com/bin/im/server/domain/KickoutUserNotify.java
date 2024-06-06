package com.bin.im.server.domain;

import java.util.HashMap;
import java.util.Map;

public class KickoutUserNotify {

   public static final int KICKOUT_REASON_RELOGIN = 1, // relogin
    KICKOUT_REASON_STATUS_ERROR = 2, // error
    KICKOUT_REASON_PC_TO_MOBILE = 3; // mobile

    private long uid;
    private long loginTime;
    private int sourceType; // old

    private int reason;
    private int fromSourceType; // new


    public KickoutUserNotify() {

    }

    public KickoutUserNotify(long uid, long loginTime, int sourceType, int reason, int fromSourceType) {
        this.uid = uid;
        this.loginTime = loginTime;
        this.sourceType = sourceType;
        this.reason = reason;
        this.fromSourceType = fromSourceType;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getFromSourceType() {
        return fromSourceType;
    }

    public void setFromSourceType(int fromSourceType) {


        this.fromSourceType = fromSourceType;
    }


    public Map<String,Object> toMap() {
        Map<String,Object> json = new HashMap<>();
        json.put("module","user");
        json.put("cmd","kickout");
        json.put("uid",uid);
        json.put("loginTime",loginTime);
        json.put("sourceType",sourceType);
        json.put("reason",reason);
        json.put("fromSourceType",fromSourceType);
        return json;
    }
}
