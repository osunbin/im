package com.bin.im.server.repositories.sql.sys;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface SystemBroadCastMsgSql {

     String table = "t_system_broadcast_msg";

     @SqlQuery("SELECT msg_id msgId,begin_ts beginTs,end_ts endTs FROM " + table + " WHERE suid = :uid and msg_id > :lastMsgId  limit :count")
     List<SystemBroadCastMsg> selectSysBroadCastMsg(@Bind("uid") long uid,@Bind("lastMsgId") long lastMsgId, @Bind("count") int count);


}
