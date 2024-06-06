package com.bin.im.server.repositories.sql.msg;

import com.bin.im.server.repositories.model.cloud.CloudMsgIdModel;
import com.bin.im.server.repositories.model.cloud.QueryMsgModel;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Map;

public interface OnlineMsgSql {

    String table = "t_user_online_msg";

    @SqlUpdate("INSERT INTO  "+table+" (msg_id,to_uid, from_uid,client_msg_id ,direction, msg_type, timestamp, msg_content) " +
            "VALUES (:msgId,:toUid,:fromUid,:clientMsgId,:direction,:msgType,:timestamp,:msgContent)")
    void saveOnlineMsg(@BindBean OnlineMsg onlineMsg);


    @SqlQuery("SELECT msg_id msgId, to_uid toUid,from_uid fromUid,client_msg_id clientMsgId,direction, msg_type msgType,timestamp,msg_Content msgContent from "+table+
            " where to_uid = :toUid and from_uid = :fromUid  <otherWhere>")
    List<OnlineMsg> selectOnlineMsg(@BindMap Map<String, ?> map, String otherWhere);

    @SqlQuery("SELECT msg_id msgId, to_uid toUid,from_uid fromUid,client_msg_id clientMsgId,direction, msg_type msgType,timestamp,msg_Content msgContent,msg_flag msgFlag from "+table+
           " where to_uid = :toUid and from_uid = :fromUid and msg_id > :lastMsgId  limit :count")
    List<OnlineMsg> selectOnlineMsg(@Bind("toUid") long toUid,@Bind("fromUid") long fromUid,@Bind("lastMsgId") long lastMsgId,@Bind("count") int count);

    @SqlQuery("SELECT COUNT(*) FROM "+table+ " force index(idx_dst_src_ts) where to_uid = :toUid and from_uid = :fromUid and direction = :direction and msg_id > :msgId")
    int selectUnreadCount(@BindBean QueryMsgModel queryMsg);

    @SqlUpdate("UPDATE "+table+ " set msg_flag = :msgFlag where to_uid = :toUid and from_uid = :fromUid and client_msg_id = :clientMsgId ")
    void backwardMsg(@BindBean OnlineMsg onlineMsg);




    @SqlUpdate("UPDATE "+table+ " set msg_content=:msgContent,msg_flag = :msgFlag where to_uid = :toUid and from_uid = :fromUid and client_msg_id = :clientMsgId ")
    void updateOnlineMsgByBackward(@BindBean CloudMsgIdModel cloudMsgIdModel);

    @SqlQuery("SELECT msg_content "+table+ "  to_uid = :toUid and from_uid = :fromUid and client_msg_id = :clientMsgId")
    String selectMsgContent(@BindBean CloudMsgIdModel cloudMsgIdModel);



}
