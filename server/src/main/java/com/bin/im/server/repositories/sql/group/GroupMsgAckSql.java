package com.bin.im.server.repositories.sql.group;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface GroupMsgAckSql {

    String table = "t_group_msg_ack";

    @SqlUpdate("INSERT INTO " + table + " (id,gid,suid,recv_uid,msg_id,is_ack,read_time) VALUES(:id,:gid,:suid,:recvUid,:msgId,:isAck,:readTime) ")
    void saveRecv(@BindBean GroupMsgAck groupMsgAck);


    @SqlUpdate("INSERT INTO " + table + " (id,gid,suid,recv_uid,msg_id) VALUES(:id,:gid,:suid,:recvUid,:msgId) ")
    void saveSend(@BindBean GroupMsgAck groupMsgAck);

    @SqlUpdate("UPDATE " + table + " SET is_ack = 1 ,read_time = readTime where gid = :gid and recv_uid = :recvUid")
    void updateAck(@Bind("gid") long gid,@Bind("recvUid") long recvUid,@Bind("readTime") long readTime);

    /**
     * 用户上线 读取群离线消息 ack
     */
    @SqlUpdate("UPDATE " + table + " SET is_ack = 1 ,read_time = readTime where gid = :gid and recv_uid = :recvUid and msg_id > :lastAckMsgId and is_ack = 0")
    void updateBatchAck(@Bind("gid") long gid,@Bind("recvUid") long recvUid,@Bind("readTime") long readTime,@Bind("lastAckMsgId") long lastAckMsgId);


    @SqlQuery("SELECT gid,recv_uid recvUid,msg_id msgId,read_time readTime FROM " + table + " WHERE gid = :gid and suid = :suid and is_ack = 0")
    List<GroupMsgAck> selectRead(@Bind("gid") long gid, @Bind("suid") long suid);






}
