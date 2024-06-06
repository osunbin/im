package com.bin.im.server.domain;

import static com.bin.im.common.GlobalCodes.RESP_SUCCESS;

public class UserLogResultModel {
    private long uid;
    private int code;

    public UserLogResultModel(long uid,int code) {
        this.uid = uid;
        this.code = code;
    }
    public static UserLogResultModel ofOk(long uid) {
        return new UserLogResultModel(uid,RESP_SUCCESS);
    }

    public static UserLogResultModel of(long uid,int code) {
       return new UserLogResultModel(uid,code);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
