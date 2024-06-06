package com.bin.im.server.repositories.sql.group;

/**
 *  用户拥有的群
 *  primary key - uid + gid
 *  sharding - uid
 */
public class UserGroups {

    private long uid;

    private long gid;
    /**
     *  加入群，群会有一个广播消息,加入的用户只能看该 joinMsgId 之后 群消息
     */
    private long joinMsgId;

    /**
     *  在线 最后一次 读取的时间
     *  TODO 数据量大 并发高  可以考虑单独一个表
     */
    private long readTime;
    /**
     * 在线 最后一次 ack msgId
     *  可以考虑单独一个表
     *  TODO  使用msgid 服务宕机/重启的1s内的消息可能不是趋势递增
     */
    private long lastAckMsgId;

    /**
     *  加入群的时间
     */
    private long joinTime;

    /**
     *  退群时间
     */
    private long quitTime;

    /**
     *  加入群的方式
     */
    private int joinType;

    /**
     *  通过谁申请加入的群
     */
    private long approvalUid;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getJoinMsgId() {
        return joinMsgId;
    }

    public void setJoinMsgId(long joinMsgId) {
        this.joinMsgId = joinMsgId;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public long getLastAckMsgId() {
        return lastAckMsgId;
    }

    public void setLastAckMsgId(long lastAckMsgId) {
        this.lastAckMsgId = lastAckMsgId;
    }


    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public long getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(long quitTime) {
        this.quitTime = quitTime;
    }

    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    public long getApprovalUid() {
        return approvalUid;
    }

    public void setApprovalUid(long approvalUid) {
        this.approvalUid = approvalUid;
    }
}
