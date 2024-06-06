package com.bin.im.server.repositories.sql.group;

/**
 *  群成员
 *  primary key - gid + uid
 *  Sharding - gid
 */
public class GroupMembers {
    /**
     *  群id
     */
    private long gid;
    /**
     *  群成员
     */
    private long uid;
    /**
     *   群主、管理员
     */
    private int utype;


    /**
     *  群消息设置-免打扰等
     */
    private int gmsgAuth;
    /**
     *  群名片  json格式
     */
    private String gcard;


    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getUtype() {
        return utype;
    }

    public void setUtype(int utype) {
        this.utype = utype;
    }


    public int getGmsgAuth() {
        return gmsgAuth;
    }

    public void setGmsgAuth(int gmsgAuth) {
        this.gmsgAuth = gmsgAuth;
    }

    public String getGcard() {
        return gcard;
    }

    public void setGcard(String gcard) {
        this.gcard = gcard;
    }
}
