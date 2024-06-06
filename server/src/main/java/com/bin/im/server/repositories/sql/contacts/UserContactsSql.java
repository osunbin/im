package com.bin.im.server.repositories.sql.contacts;

import com.bin.im.server.domain.ContactUnreadCountInfo;
import com.bin.im.server.domain.ContactsAckMsgInfo;
import com.bin.im.server.domain.ContactsReadedInfo;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface UserContactsSql {

    String table = "t_user_contact";
    // 查询好友列表未读数
    @SqlQuery("SELECT uidB contactUid,unread_count msgCount FROM "+table+"uidA = :uidA and contacts_flag = 0 and unread_count > 0")
    List<ContactUnreadCountInfo> selectUnreadCount(@Bind("uidA") long uidA);

    @SqlUpdate("UPDATE "+table+ " SET  unread_count = :unreadCount where uidA = :uidA and uidB = :uidB ")
    void updateUnread(@Bind("uidA")long uidA,@Bind("uidB") long uidB,@Bind("unreadCount") int unreadCount);

    @SqlUpdate("UPDATE "+table+ " SET contacts_flag = :contactsFlag where uidA = :uidA and uidB = :delUid")
    void deleteContacts(@Bind("uidA")long uidA,@Bind("delUid") long delUid,@Bind("contactsFlag") int contactsFlag);

    /**
     *  接收方 更新
     */
    @SqlQuery("SELECT uidB contactUid,read_time readTime  FROM "+table+ " WHERE uidA = :uidA and uidB in (<uidBList>)")
    List<ContactsReadedInfo> selectReadMsgTime(@Bind("uidA")  long uidA ,@BindList("uidBList")List<Long> uidBList);

    @SqlQuery("SELECT uidB contactUid,last_ack_msgId lastAckMsgId  FROM "+table+ " WHERE uidA = :uidA and uidB in (<uidBList>) ")
    List<ContactsAckMsgInfo> selectAckMsgId(@Bind("uidA")  long uidA, @BindList("uidBList")List<Long> uidBList);


    @SqlUpdate("UPDATE  "+table+ " SET last_ack_msgId = :lastAckMsgId, last_time =:lastTime WHERE uidA = :uidA and uidB = :uidB")
    void updateOnlineContacts(@BindBean UserContacts userContacts);

    @SqlUpdate("UPDATE  "+table+ " SET unread_count = unread_count + :unreadCount, last_time =:lastTime WHERE uidA = :uidA and uidB = :uidB")
    void updateOfflineContacts(@BindBean UserContacts userContacts);


    @SqlUpdate("INSERT INTO " + table + " (uidA,uidB,direction,add_time) VALUES(:uidA,:uidB,:direction,:addTime) ")
    void saveContacts(@BindBean UserContacts userContacts);

    @SqlQuery("SELECT uidB FROM "+table+ " where uidA = :uidA and contacts_flag = :contactsFlag")
    List<Long> selectContactsId(@Bind("uidA") long uidA, @Bind("contactsFlag") int contactsFlag);

    @SqlQuery("SELECT uidB,last_ack_msgId lastAckMsgId,read_time readTime,unread_count unreadCount,last_time lastTime，add_time addTime FROM "+table+" WHERE uidA=:uidA and last_time >:lastTime and contacts_flag=:contactsFlag   order by last_time DESC  limit :count ")
    List<UserContacts> selectContactsByTime(@Bind("uidA")long uidA,@Bind("lastTime")long lastTime, @Bind("contactsFlag") int contactsFlag,@Bind("count") int count);

    @SqlQuery("SELECT uidB,last_ack_msgId lastAckMsgId,read_time readTime,unread_count unreadCount,last_time lastTime，add_time addTime FROM "+table+" WHERE uidA=:uidA and uidB in (<uidBList>)")
    List<UserContacts> selectFixedContacts(@Bind("uidA") long uidA,@BindList("uidBList") List<Long> uidBList);

    @SqlQuery("SELECT uidB,last_ack_msgId lastAckMsgId,read_time readTime,unread_count unreadCount,last_time lastTime，add_time addTime FROM "+table+" WHERE uidA=:uidA and uidB = :uidB ")
    UserContacts selectFixedContacts(@Bind("uidA") long uidA,@BindList("uidB") long uidB);


    @SqlUpdate("UPDATE  "+table+ " SET last_ack_msgId = :lastAckMsgId,read_time = :readTime,unread_count = :unreadCount,last_time =:lastTime where uidA = :uidA and uidB = :uidB")
    void updateSendTime(@BindBean UserContacts userContacts);

    @SqlUpdate("UPDATE  "+table+ " SET last_ack_msgId = :lastAckMsgId,read_time = :readTime,unread_count = unread_count + :unreadCount,last_time =:lastTime where uidA = :uidA and uidB = :uidB")
    void updateRecvTime(@BindBean UserContacts userContacts);

    @SqlUpdate("UPDATE  "+table+ " set read_time=:readTime,last_ack_msgId = :lastAckMsgId where uidA =:uidA and uidB=:uidB ")
    void updateReadTime(@Bind("uidA")long uidA,@Bind("uidB")  long uidB,@Bind("readTime") long readTime,@Bind("lastAckMsgId") long lastAckMsgId);

    @SqlUpdate("UPDATE  "+table+ " set read_time=:readTime where uidA =:uidA and uidB=:uidB ")
    void updateReadTime(@Bind("uidA")long uidA,@Bind("uidB")  long uidB,@Bind("readTime") long readTime);



    @SqlQuery("SELECT uidB,unread_count unreadCount,last_time lastTime，add_time addTime FROM "+table+" force index(idx_uidA_ts) WHERE uidA=:uidA and add_time >:startTime and add_time <:endTime and contacts_flag = 0  order by add_time DESC  limit :off,:lim ")
    List<UserContacts> selectNewContacts(@Bind("uidA")long uidA,@Bind("startTime")long startTime,@Bind("endTime") long endTime,@Bind("off") int off,@Bind("lim") int lim);

    @SqlQuery("SELECT COUNT(*) FROM "+table+ "  force index(idx_uidA_ts) WHERE uidA=:uidA and add_time >:startTime and add_time <:endTime and contacts_flag = 0")
    int selectNewContactsCount(@Bind("uidA")long uidA,@Bind("startTime")long startTime,@Bind("endTime") long endTime);

    @SqlQuery("SELECT uidB contactUid,unread_count msgCount FROM "+table+ " where uidA=:uidA and uidB in (<uidBList>)")
    List<ContactUnreadCountInfo> selectUnreadCount(@Bind("uidA") long uidA,@BindList("uidBList") List<Long> uidBList);

    @SqlQuery("SELECT uidB,last_ack_msgId lastAckMsgId,read_time readTime,unread_count unreadCount,last_time lastTime FROM "+table+ " where uidA=:uidA and last_time <= :lastTime    order by last_time DESC  limit :count ")
    List<UserContacts> selectAllContacts(@Bind("uidA") long uidA,@Bind("lastTime") long lastTime,@Bind("count") int count);

    @SqlQuery("SELECT uidB,last_ack_msgId lastAckMsgId,read_time readTime,unread_count unreadCount,last_time lastTime FROM "+table+ " where uidA=:uidA and last_time <= :lastTime and contacts_flag = :contactsFlag  order by last_time DESC  limit :count ")
    List<UserContacts> selectContacts(@Bind("uidA") long uidA,@Bind("lastTime") long lastTime,@Bind("contactsFlag") int contactsFlag,@Bind("count") int count);








}

