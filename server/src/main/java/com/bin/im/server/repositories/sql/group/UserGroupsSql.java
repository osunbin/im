package com.bin.im.server.repositories.sql.group;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface UserGroupsSql {

    String table = "t_user_groups";


    @SqlUpdate("INSERT INTO " + table + " (uid,gid,read_time,join_msgId,last_ack_msgId,join_time,join_type,approval_uid) " +
            " VALUES(:uid,:gid,:joinMsgId,:readTime,:lastAckMsgId,:joinTime,:quitTime,:joinType,:approvalUid)")
    void save(@BindBean UserGroups userGroups);

    @SqlUpdate("UPDATE " + table + " SET read_time = :readTime,last_ack_msgId = :lastAckMsgId where uid = :uid and gid = :gid ")
    void updateReadTime(@Bind("uid") long uid,@Bind("gid") long gid,@Bind("readTime") long readTime,@Bind("lastAckMsgId") long lastAckMsgId);

    @SqlQuery("SELECT gid,join_msgId joinMsgId,read_time readTime,last_ack_msgId lastAckMsgId FROM " + table + " where uid = :uid")
    List<UserGroups> selectLast(@Bind("uid") long uid);

    @SqlQuery("SELECT gid,join_msgId joinMsgId,read_time readTime,last_ack_msgId lastAckMsgId FROM " + table + " where uid = :uid and gid in (<gidList>)")
    List<UserGroups> selectLast(@Bind("uid") long uid,@BindList("gidList") List<Long> gidList);


}
