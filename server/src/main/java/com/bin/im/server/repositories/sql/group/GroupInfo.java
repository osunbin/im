package com.bin.im.server.repositories.sql.group;

/**
 *  群的元信息
 *  index key - guid、gtype+gid
 */
public class GroupInfo {
    /**
     *  群id
     */
    private long gid;
    /**
     *  类型：1-群、2-多人  方便扩展
     */
    private int gtype;
    /**
     *  创建群的人
     */
    private long guid;
    /**
     *  群名称
     */
    private String gname;
    /**
     *  群简介
     */
    private String gintro;

    /**
     *  群公告
     */
    private String gboard;
    /**
     *  群邀请请权限：任何人都可以邀请,只能二维码,只能群主邀请
     */
    private int gauth;
    /**
     *  创建时间
     */
    private long gtime;



    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public int getGtype() {
        return gtype;
    }

    public void setGtype(int gtype) {
        this.gtype = gtype;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGintro() {
        return gintro;
    }

    public void setGintro(String gintro) {
        this.gintro = gintro;
    }

    public String getGboard() {
        return gboard;
    }

    public void setGboard(String gboard) {
        this.gboard = gboard;
    }

    public int getGauth() {
        return gauth;
    }

    public void setGauth(int gauth) {
        this.gauth = gauth;
    }

    public long getGtime() {
        return gtime;
    }

    public void setGtime(long gtime) {
        this.gtime = gtime;
    }
}
