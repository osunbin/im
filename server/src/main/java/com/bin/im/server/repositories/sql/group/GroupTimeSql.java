package com.bin.im.server.repositories.sql.group;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface GroupTimeSql {

    String table = "t_group_time";

    @SqlQuery("SELECT gid,ginfo_time ginfoTime,gulist_time gulistTime,gmsg_time gmsgTime FROM "+table+ " where gid in (<groupList>)")
    List<GroupTime> selectGroupTime(@BindList("groupList") List<Long> groupList);

    @SqlUpdate("UPDATE "+table+ " SET ginfo_time = :ginfoTime WHERE gid = :gid")
    void updateInfoTime(@Bind("gid") long gid,@Bind("ginfoTime") long ginfoTime);

    @SqlUpdate("UPDATE "+table+ " SET gulist_time = :gulistTime WHERE gid = :gid")
    void updateListTime(@Bind("gid") long gid,@Bind("gulistTime") long gulistTime);


    @SqlUpdate("UPDATE "+table+ " SET gmsg_time = :gmsgTime WHERE gid = :gid")
    void updateMsgTime(@Bind("gid") long gid,@Bind("gmsgTime") long gmsgTime);


}
