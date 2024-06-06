package com.bin.im.server.repositories.sql.group;

/**
 *  上线时根据时间戳增量拉取
 */
public class GroupTime {

    /**
     *  群id
     */
    private long gid;

    /**
     *  群 元信息 最后一次 更新时间
     */
    private long ginfoTime;

    /**
     *  群成员列表 最后一次 更新时间
     */
    private long gulistTime;

    /**
     *  最后一次消息的时间(包括离线)
     */
    private long gmsgTime;


    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getGinfoTime() {
        return ginfoTime;
    }

    public void setGinfoTime(long ginfoTime) {
        this.ginfoTime = ginfoTime;
    }

    public long getGulistTime() {
        return gulistTime;
    }

    public void setGulistTime(long gulistTime) {


        this.gulistTime = gulistTime;
    }


    public long getGmsgTime() {
        return gmsgTime;
    }

    public void setGmsgTime(long gmsgTime) {
        this.gmsgTime = gmsgTime;
    }
}
