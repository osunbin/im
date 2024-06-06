package com.bin.im.server.core.msg;

import com.bin.im.common.internal.utils.CollectionUtil;
import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.domain.BatchUserMsg;
import com.bin.im.server.domain.ContactsAckMsgInfo;
import com.bin.im.server.domain.ContactsLastAckMsg;
import com.bin.im.server.domain.OnlineMsgInfo;
import com.bin.im.server.domain.UserUnreadMsg;
import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.repositories.sql.msg.OnlineMsg;
import com.bin.im.server.spi.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OnlineMsgHandler extends BaseHandler {

    // 根据msgId  拉取消息 ack
    public void fetchOnlineMsg(UserUnreadMsg unreadMsg) {
        long fromUid = unreadMsg.getFromUid();
        List<Long> toUids = unreadMsg.getToUids();
        if (CollectionUtil.isEmpty(toUids)) return;
        List<ContactsAckMsgInfo> contactsAckMsgInfo = imDas().queryMarkAckMsgId(fromUid, toUids).getMsg();
        if (CollectionUtil.isEmpty(contactsAckMsgInfo)) return;

        List<ContactsLastAckMsg> contactsLastAckMsgs = contactsAckMsgInfo.stream().
                map(ackMsg -> new ContactsLastAckMsg(ackMsg.getContactUid(), ackMsg.getLastMsgId())).collect(Collectors.toList());

        BatchUserMsg batchUserMsg = new BatchUserMsg(fromUid, contactsLastAckMsgs);
        batchUserMsg.initStatus();
        fetchOnlineMsg(batchUserMsg);
    }


    public List<OnlineMsgInfo> fetchOnlineMsg(BatchUserMsg batchUnreadMsg) {
        List<ContactsLastAckMsg> contactsLastAckMsgs =
                     batchUnreadMsg.getContactsLastAckMsgs();
        List<OnlineMsgInfo> allOnlineMsgs = new ArrayList<>();

        if (CollectionUtil.isEmpty(contactsLastAckMsgs)) return allOnlineMsgs;

        long currTime = TimeUtils.currTimeMillis();

        List<UserContacts> userContactsList = new ArrayList<>();
        long fromUid = batchUnreadMsg.getFromUid();
        for (ContactsLastAckMsg lastAckMsg : contactsLastAckMsgs) {
            long contactUid = lastAckMsg.getContactUid();
            int count = lastAckMsg.getCount();

            List<OnlineMsg> onlineMsgs = imDas().queryOnlineMsg(fromUid, contactUid, lastAckMsg.getLastMsgId(), count).getMsg();

            for (OnlineMsg onlineMsg : onlineMsgs) {
                if (!onlineMsg.effective()) continue;
                long msgId = onlineMsg.getMsgId();
                OnlineMsgInfo onlineMsgInfo = new OnlineMsgInfo();
                onlineMsgInfo.setMsgId(msgId);
                onlineMsgInfo.setClientMsgId(onlineMsg.getClientMsgId());
                onlineMsgInfo.setTimestamp(onlineMsg.getTimestamp());
                onlineMsgInfo.setMsgContent(onlineMsg.getMsgContent());
                if (onlineMsg.forward()) { // 正向
                    onlineMsgInfo.setFromUid(onlineMsg.getFromUid());
                    onlineMsgInfo.setToUid(onlineMsg.getToUid());
                } else {
                    onlineMsgInfo.setFromUid(onlineMsg.getToUid());
                    onlineMsgInfo.setToUid(onlineMsg.getFromUid());
                }
                allOnlineMsgs.add(onlineMsgInfo);

            }

            UserContacts userContacts = new UserContacts();
            userContacts.setLastAckMsgId(lastAckMsg.getLastMsgId());
            userContacts.setLastTime(currTime);
            userContacts.setReadTime(currTime);
            userContacts.setUidA(fromUid);
            userContacts.setUidB(contactUid);
            userContactsList.add(userContacts);
        }

        if (!batchUnreadMsg.first()) {
            imDas().batchUpdateOnlineSingleContactReceive(userContactsList);
            userContactsList.clear();
        }

       return allOnlineMsgs;
    }


}
