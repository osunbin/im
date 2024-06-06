package com.bin.im.server.repositories.sql.sm;

import com.bin.im.server.repositories.model.cloud.CloudMsgTimeModel;
import com.bin.im.server.repositories.model.sm.SmOnlineMsgExtModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SmOnlineMsgSql {

    String table = "t_sm_online_msg";

    @SqlUpdate("INSERT INTO  "+table+" (msg_id,to_uid, from_uid,sm_uid,client_msg_id ,direction,uid_sm, msg_type, timestamp, msg_content,info_id) " +
            "VALUES (:msgId,:toUid,:fromUid,:smUid,:clientMsgId,:direction,:uidSm,:msgType,:timestamp,:msgContent,:infoId)")
    void saveOnlineMsg(@BindBean SmOnlineMsg userSmOnlineMsg);


    @SqlUpdate("INSERT INTO  "+table+" (msg_id,to_uid, from_uid,sm_uid,client_msg_id ,direction, msg_type, timestamp, msg_content,info_id) " +
            "VALUES (:msgId,:toUid,:fromUid,:smUid,:clientMsgId,:direction,:msgType,:timestamp,:msgContent,:infoId)")
    void save2BOnlineMsg(@BindBean SmOnlineMsgExtModel smOnlineMsg);


    @SqlQuery("SELECT msg_id msgId, to_uid toUid,from_uid fromUid,sm_uid smUid,client_msg_id clientMsgId,direction, msg_type msgType,timestamp,msg_Content msgContent,info_id infoId FROM "+table+
            " to_uid = :toUid and from_uid = :fromUid and timestamp>:timestamp  order by timestamp ASC limit :count")
    void select2BOnlineMsg(@BindBean CloudMsgTimeModel cloudMsgTimeModel);


}
