package com.bin.im.server.repositories.sql.sys;

import com.bin.im.server.repositories.model.QueryBaseMsgModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface SystemMsgSql {

    String table = "t_user_sys_msg";

    @SqlQuery("SELECT msg_id msgId, to_uid toUid, from_uid fromUid, msg_type msgType, msg_content msgContent,timestamp  FROM " + table + " WHERE to_uid = :toUid and from_uid = :fromUid  and msg_id > :lastMsgId  limit :count")
    List<SystemMsg> selectSysMsg(@BindBean QueryBaseMsgModel queryMsgModel);

    @SqlUpdate("INSERT INTO " + table + " (msg_id,to_uid,from_uid,msg_type,msg_content,timestamp) VALUES(:msgId,:toUid,:fromUid,:msgType,:msgContent,:timestamp)")
    void saveSysMsg(@BindBean SystemMsg systemMsg);



}
