package com.bin.im.server.repositories.sql.user;

public class UserTeam {

    private long uid;
    private long tid;
    /**
     *   名称
     */
    private String tname;
    /**
     *  标签
     */
    private String tag;

    /**
     *  uid、gid、gsid
     */
    private long id;
    /**
     *  1-好友、2-群组、3-分组
     */
    private long teamType;

    private long createTime;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeamType() {
        return teamType;
    }

    public void setTeamType(long teamType) {
        this.teamType = teamType;
    }
}
