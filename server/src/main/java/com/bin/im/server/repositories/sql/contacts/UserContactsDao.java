package com.bin.im.server.repositories.sql.contacts;

import com.bin.im.server.domain.ContactUnreadCountInfo;
import com.bin.im.server.domain.ContactsAckMsgInfo;
import com.bin.im.server.domain.ContactsReadedInfo;
import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.model.contacts.QueryContactsKefuModel;
import com.bin.im.server.repositories.model.contacts.QueryContactsModel;
import com.bin.im.server.repositories.sql.BaseDao;
import org.jdbi.v3.core.statement.PreparedBatch;


import java.util.List;

public class UserContactsDao extends BaseDao {





    public void addContacts(UserContacts userContacts) {
        getJdbi().useExtension(UserContactsSql.class, dao -> dao.saveContacts(userContacts));
    }


    public DasResultModel<List<ContactUnreadCountInfo>>  queryUnreadCount(long uidA, List<Long> uidBList) {
        List<ContactUnreadCountInfo> contactUnreadCountInfos =  getJdbi().withExtension(UserContactsSql.class,
                dao -> dao.selectUnreadCount(uidA, uidBList));
        return DasResultModel.ofOk(contactUnreadCountInfos);
    }

    public DasResultModel<List<ContactUnreadCountInfo>>  queryUnreadCount(long uidA) {
        List<ContactUnreadCountInfo> contactUnreadCountInfos = getJdbi().withExtension(UserContactsSql.class,
                dao -> dao.selectUnreadCount(uidA));
        return DasResultModel.ofOk(contactUnreadCountInfos);
    }


    public void updateUnread(long uidA, long uidB, int unreadCount) {
        getJdbi().useExtension(UserContactsSql.class,
                dao -> dao.updateUnread(uidA,uidB,unreadCount));
    }


    public DasResultModel<List<ContactsAckMsgInfo>> queryMarkAckMsgId(long uidA, List<Long> uidBList) {
        List<ContactsAckMsgInfo> contactsAckMsgInfos = getJdbi().withExtension(UserContactsSql.class, dao -> dao.selectAckMsgId(uidA, uidBList));
        return DasResultModel.ofOk(contactsAckMsgInfos);
    }

    public DasResultModel<List<ContactsReadedInfo>> queryMarkReadMsg(long uidA, List<Long> uidBList) {
        List<ContactsReadedInfo> contactsReadedInfos = getJdbi().withExtension(UserContactsSql.class, dao -> dao.selectReadMsgTime(uidA, uidBList));
        return DasResultModel.ofOk(contactsReadedInfos);
    }


    public DasResultModel<List<Long>> queryContactsUid(long uidA,int contacts) {

        List<Long> uidBs = getJdbi().withExtension(UserContactsSql.class, dao -> dao.selectContactsId(uidA,contacts));

        return DasResultModel.ofOk(uidBs);
    }



    public DasResultModel<List<UserContacts>> queryRangeContacts(QueryContactsModel queryContacts) {
        long uidA = queryContacts.getUidA();
        int count = queryContacts.getCount();
        long timestamp = queryContacts.getTimestamp();

        List<UserContacts> userContacts =
                getJdbi().withExtension(UserContactsSql.class, dao -> dao.selectContactsByTime(uidA, timestamp, 0, count));

        return DasResultModel.ofOk(userContacts);
    }


    public DasResultModel<List<UserContacts>> queryFixedContacts(long uidA, List<Long> uidBList) {

        List<UserContacts> userContacts =
                getJdbi().withExtension(UserContactsSql.class, dao -> dao.selectFixedContacts(uidA, uidBList));

        return DasResultModel.ofOk(userContacts);
    }

    public DasResultModel<UserContacts> queryFixedContacts(long uidA, long uidB) {

        UserContacts userContacts =
                getJdbi().withExtension(UserContactsSql.class, dao -> dao.selectFixedContacts(uidA, uidB));

        return DasResultModel.ofOk(userContacts);
    }


    /**
     *  发消息
     */
    public void updateSingleContactSended(UserContacts userContacts) {
        getJdbi().useExtension(UserContactsSql.class, dao -> dao.updateOnlineContacts(userContacts));
    }


    public void batchUpdateOnlineSingleContactReceive(List<UserContacts> userContacts) {

        getJdbi().withHandle(handle -> {
            PreparedBatch preparedBatch =
                    handle.prepareBatch("UPDATE  " + UserContactsSql.table +
                            " SET last_ack_msgId = :lastAckMsgId,unread_count = 0,read_time = :readTime, last_time =:lastTime WHERE uidA = :uidA and uidB = :uidB");
            preparedBatch.bindBean(userContacts);
           return preparedBatch.execute();
        });
    }

    public void updateOnlineSingleContactReceive(UserContacts userContacts) {
        getJdbi().useExtension(UserContactsSql.class, dao -> dao.updateOnlineContacts(userContacts));
    }


    public void updateOfflineSingleContactReceive(UserContacts userContacts) {
        getJdbi().useExtension(UserContactsSql.class, dao -> dao.updateOfflineContacts(userContacts));
    }



    public void deleteSingleContacts(long uidA,long delUid,int contacts) {
        getJdbi().useExtension(UserContactsSql.class, dao -> dao.deleteContacts(uidA,delUid,contacts));
    }



    public void updateSingleReadedContacts(long uidA, long uidB, long readTime,long lastAckMsgId) {
        getJdbi().useExtension(UserContactsSql.class,
                dao -> {
                    if (lastAckMsgId > 0) {
                        dao.updateReadTime(uidA,uidB,readTime,lastAckMsgId);
                    }else {
                        dao.updateReadTime(uidA,uidB,readTime);
                    }
                }
        );
    }


    public DasResultModel<List<UserContacts>> queryNewContacts(QueryContactsKefuModel queryContactsKefu) {
        long uidA = queryContactsKefu.getUidA();
        long startTime = queryContactsKefu.getStartTime();
        long endTime = queryContactsKefu.getEndTime();
        int pageNum = queryContactsKefu.getPageNum();
        int pageSize = queryContactsKefu.getPageSize();

        List<UserContacts> userContacts = getJdbi().withExtension(UserContactsSql.class,
                dao -> dao.selectNewContacts(uidA, startTime, endTime, ((pageNum - 1) * pageSize), pageSize));
        return DasResultModel.ofOk(userContacts);
    }


    public DasResultModel<Integer> queryContactsCount(QueryContactsKefuModel queryContactsKefu) {
        long uidA = queryContactsKefu.getUidA();
        long startTime = queryContactsKefu.getStartTime();
        long endTime = queryContactsKefu.getEndTime();
        int newCount = getJdbi().withExtension(UserContactsSql.class,
                dao -> dao.selectNewContactsCount(uidA, startTime, endTime));
        return DasResultModel.ofOk(newCount);
    }


    public DasResultModel<List<UserContacts>> queryContacts(QueryContactsModel queryContacts) {
        long uidA = queryContacts.getUidA();
        long timestamp = queryContacts.getTimestamp();
        int count = queryContacts.getCount();
        int contacts = 0;
        List<UserContacts> userContacts = getJdbi().withExtension(UserContactsSql.class,
                dao -> dao.selectContacts(uidA, timestamp, contacts, count));
        return DasResultModel.ofOk(userContacts);
    }


    public DasResultModel<List<UserContacts>> queryAllContacts(QueryContactsModel queryContacts) {
        long uidA = queryContacts.getUidA();
        long timestamp = queryContacts.getTimestamp();
        int count = queryContacts.getCount();
        List<UserContacts> userContacts = getJdbi().withExtension(UserContactsSql.class,
                dao -> dao.selectAllContacts(uidA, timestamp,  count));
        return DasResultModel.ofOk(userContacts);
    }




}
