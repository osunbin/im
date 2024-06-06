package com.bin.im.server.repositories.sql.sys;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface SystemMsgContentSql {


    @SqlQuery("SELECT msg_id msgId,msg_content msgContent t_system_msg_content from where msg_id in (<msgIdList>)")
    List<SystemMsgContent> selectSysMsgContents(@BindList("msgIdList") List<Long> msgIdList);

    @SqlQuery("SELECT msg_id msgId,msg_content msgContent t_system_msg_content from where msg_id = :msgId ")
    SystemMsgContent selectSysMsgContent(@Bind("msgId") long msgId);
}
