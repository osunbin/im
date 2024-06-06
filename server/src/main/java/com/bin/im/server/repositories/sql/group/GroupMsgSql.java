package com.bin.im.server.repositories.sql.group;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface GroupMsgSql {

    String table = "t_group_msg";

    @SqlUpdate("INSERT INTO " + table + " (gid,msg_id,suid,msg_type,msg_content,send_time,client_msg_id) VALUES(:gid,:msgId,:suid,:msgType,:msgContent,:sendTime,:clientMsgId) ")
    void save(@BindBean GroupMsg groupMsg);

    @SqlQuery("SELECT msg_id msgId,suid,msg_type msgType,msg_content msgContent,client_msg_id clientMsgId FROM " + table + " WHERE gid = :gid and msg_id = :msgId")
    void selectOneGroupMsg(@Bind("gid") long gid,@Bind("msgId") long msgId);


    @SqlQuery("SELECT msg_id msgId,suid,msg_type msgType,msg_content msgContent,client_msg_id clientMsgId FROM " + table + " WHERE gid = :gid and msg_id > :msgId ORDER BY gid,msg_id DESC limit :count")
    List<GroupMsg> selectGroupMsgByMsgId(@Bind("gid") long gid, @Bind("msgId") long msgId,@Bind("count") int count);

    @SqlQuery("SELECT msg_id msgId,suid,msg_type msgType,msg_content msgContent,client_msg_id clientMsgId FROM " + table + " WHERE gid = :gid and send_time >= :sendTime ORDER BY gid,send_time DESC limit :count")
    List<GroupMsg> selectGroupMsgByTime(@Bind("gid") long gid,@Bind("sendTime") long sendTime,@Bind("count") int count);





}
