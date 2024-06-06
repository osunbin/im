package com.bin.im.server.repositories;

import com.bin.im.common.internal.utils.BiTuple;
import com.bin.im.server.domain.ContactsAckMsgInfo;
import com.bin.im.server.domain.ContactsReadedInfo;
import com.bin.im.server.repositories.model.DasResultModel;
import com.bin.im.server.repositories.model.QueryBaseMsgModel;
import com.bin.im.server.repositories.model.cloud.TOBOnlineMsgExtModel;
import com.bin.im.server.repositories.model.contacts.UserContactModel;
import com.bin.im.server.repositories.model.sm.SmContactsModel;
import com.bin.im.server.repositories.model.sm.SmOnlineMsgExtModel;
import com.bin.im.server.repositories.model.user.UserDevTokenModel;
import com.bin.im.server.repositories.model.user.UserLoginModel;
import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.repositories.sql.msg.OnlineMsg;
import com.bin.im.server.repositories.sql.sm.SmOnlineMsg;
import com.bin.im.server.repositories.sql.sys.SystemBroadCastMsg;
import com.bin.im.server.repositories.sql.sys.SystemMsg;
import com.bin.im.server.repositories.sql.sys.SystemMsgContent;
import com.bin.im.server.repositories.sql.user.UserLoginBitMap;

import java.util.List;

public interface ImDas {

    int onlineMsgSize = 10;



    DasResultModel<UserContacts> queryFixedContacts(long uidA, long uidB);

    DasResultModel<List<UserContacts>> queryFixedContacts(long uidA, List<Long> uidBList);






    void upsertLoginData(UserLoginModel userLogin);


    void upsertLogoutData(UserLoginModel userLogin);



    void upsertLoginBitMap(UserLoginBitMap userLoginBitMap);


    DasResultModel<UserLoginBitMap> queryLoginBitMap(long uid);



    void upsertUserDeviceId(UserDevTokenModel userDevToken);


    void batchUpdateOnlineSingleContactReceive(List<UserContacts> userContacts);

    void updateSingleContactSended(UserContacts userContacts);

    void updateOnlineSingleContactReceive(UserContacts userContacts);

    void updateOfflineSingleContactReceive(UserContacts userContacts);

    DasResultModel<List<ContactsAckMsgInfo>> queryMarkAckMsgId(long uidA, List<Long> uidBList);

    DasResultModel<List<ContactsReadedInfo>> queryMarkReadMsg(long uidA, List<Long> uidBList);

    DasResultModel<List<SystemMsg>> querySysMsg(QueryBaseMsgModel queryMsgModel);

    DasResultModel<Long> saveSysMsg(SystemMsg systemMsg);

    DasResultModel<List<SystemBroadCastMsg>> querySystemBroadCastMsg(long uid, long lastMsgId, int count);

    DasResultModel<List<SystemMsgContent>> querySysMsgContents(List<Long> msgIdList);

    DasResultModel<SystemMsgContent> queryOneSysMsgContent(long msgId);

    void backwardMsg(OnlineMsg onlineMsg);


    DasResultModel<List<OnlineMsg>> queryOnlineMsg(long toUid, long fromUid, long lastMsgId, int count);

    DasResultModel<Long> saveOnlineMsg(OnlineMsg onlineMsg);

    DasResultModel<Long> saveOnlineMsg(SmOnlineMsg onlineMsg);
}
