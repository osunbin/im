package com.bin.im.server.repositories;

import com.bin.im.server.domain.ContactsAckMsgInfo;
import com.bin.im.server.domain.ContactsReadedInfo;
import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.model.QueryBaseMsgModel;
import com.bin.im.server.repositories.model.user.UserDevTokenModel;
import com.bin.im.server.repositories.model.user.UserLoginModel;
import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.repositories.sql.contacts.UserContactsDao;
import com.bin.im.server.repositories.sql.msg.OnlineMsg;
import com.bin.im.server.repositories.sql.msg.OnlineMsgDao;
import com.bin.im.server.repositories.sql.sm.SmOnlineMsgDao;
import com.bin.im.server.repositories.sql.sm.SmOnlineMsg;
import com.bin.im.server.repositories.sql.sys.SystemBroadCastMsg;
import com.bin.im.server.repositories.sql.sys.SystemMsg;
import com.bin.im.server.repositories.sql.sys.SystemMsgContent;
import com.bin.im.server.repositories.sql.sys.SystemMsgDao;
import com.bin.im.server.repositories.sql.user.UserDao;
import com.bin.im.server.repositories.sql.user.UserLoginBitMap;
import com.bin.im.server.spi.impl.ImEngineImpl;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ImDasImpl implements ImDas {

    private ImEngineImpl imEngine;
    private UserDao userDao;
    private OnlineMsgDao onlineMsgDao;
    private UserContactsDao userContactsDao;
    private SystemMsgDao systemMsgDao;
    private SmOnlineMsgDao smOnlineMsgDao;
    public ImDasImpl(ImEngineImpl imEngine) {
        this.imEngine = imEngine;
        Jdbi jdbi = imEngine.getJdbi();
        onlineMsgDao = new OnlineMsgDao();
        userContactsDao = new UserContactsDao();

        systemMsgDao = new SystemMsgDao();
        smOnlineMsgDao = new SmOnlineMsgDao();

        userDao = new UserDao();

        onlineMsgDao.setJdbi(jdbi);
        userContactsDao.setJdbi(jdbi);


        systemMsgDao.setJdbi(jdbi);
        smOnlineMsgDao.setJdbi(jdbi);

        userDao.setJdbi(jdbi);


    }











    public DasResultModel<UserContacts> queryFixedContacts(long uidA, long uidB) {
       return userContactsDao.queryFixedContacts(uidA,uidB);
    }

    public DasResultModel<List<UserContacts>> queryFixedContacts(long uidA, List<Long> uidBList) {
        return userContactsDao.queryFixedContacts(uidA,uidBList);
    }









    public void upsertLoginData(UserLoginModel userLogin) {
        userDao.upsertLoginData(userLogin);
    }

    public void upsertLogoutData(UserLoginModel userLogin) {
        userDao.upsertLogoutData(userLogin);
    }


    public void upsertLoginBitMap(UserLoginBitMap userLoginBitMap) {

        userDao.upsertLoginBitMap(userLoginBitMap);
    }

    public DasResultModel<UserLoginBitMap> queryLoginBitMap(long uid) {
        return userDao.queryLoginBitMap(uid);
    }


    public void upsertUserDeviceId(UserDevTokenModel userDevToken) {
        userDao.upsertUserDeviceId(userDevToken);
    }



    public void batchUpdateOnlineSingleContactReceive(List<UserContacts> userContacts) {
        userContactsDao.batchUpdateOnlineSingleContactReceive(userContacts);
    }

    public void updateSingleContactSended(UserContacts userContacts) {
        userContactsDao.updateSingleContactSended(userContacts);
    }

    public void updateOnlineSingleContactReceive(UserContacts userContacts) {
        userContactsDao.updateOnlineSingleContactReceive(userContacts);
    }

    public void updateOfflineSingleContactReceive(UserContacts userContacts) {
        userContactsDao.updateOfflineSingleContactReceive(userContacts);
    }

    public DasResultModel<List<ContactsAckMsgInfo>> queryMarkAckMsgId(long uidA, List<Long> uidBList) {
        return userContactsDao.queryMarkAckMsgId(uidA, uidBList);
    }

    public DasResultModel<List<ContactsReadedInfo>> queryMarkReadMsg(long uidA, List<Long> uidBList) {
        return userContactsDao.queryMarkReadMsg(uidA, uidBList);
    }

    public  DasResultModel<List<SystemMsg>> querySysMsg(QueryBaseMsgModel queryMsgModel) {
       return systemMsgDao.querySysMsg(queryMsgModel);
    }

    public DasResultModel<Long> saveSysMsg(SystemMsg systemMsg) {

       return systemMsgDao.saveSysMsg(systemMsg);
    }

    public DasResultModel<List<SystemBroadCastMsg>> querySystemBroadCastMsg(long uid, long lastMsgId, int count) {
      return   systemMsgDao.querySystemBroadCastMsg(uid,lastMsgId,count);
    }

    public DasResultModel<List<SystemMsgContent>> querySysMsgContents(List<Long> msgIdList) {
        return systemMsgDao.querySysMsgContents(msgIdList);
    }

    public DasResultModel<SystemMsgContent> queryOneSysMsgContent(long msgId) {
        return systemMsgDao.querySysMsgContent(msgId);
    }

    public void backwardMsg(OnlineMsg onlineMsg) {
        onlineMsgDao.backwardMsg(onlineMsg);
    }


    public DasResultModel<List<OnlineMsg>> queryOnlineMsg(long toUid,long fromUid,long lastMsgId,int count) {
        return onlineMsgDao.queryOnlineMsg(toUid,fromUid,lastMsgId,count);
    }

    public DasResultModel<Long> saveOnlineMsg(OnlineMsg onlineMsg) {
        return onlineMsgDao.saveOnlineMsg(onlineMsg);
    }


    public DasResultModel<Long> saveOnlineMsg(SmOnlineMsg onlineMsg) {
        return smOnlineMsgDao.saveOnlineMsg(onlineMsg);
    }
}

