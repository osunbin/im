package com.bin.im.server.core.sys;


import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.server.common.type.ServiceType;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.cache.BroadcastMsgCache;
import com.bin.im.server.core.msg.BroadcastMsgMemoryHandler;
import com.bin.im.server.domain.ContactPreloadSysMsg;
import com.bin.im.server.domain.ContactsAckMsgInfo;
import com.bin.im.server.domain.OnlineMsgInfo;
import com.bin.im.server.domain.PreloadSysMsg;
import com.bin.im.server.repositories.model.QueryBaseMsgModel;
import com.bin.im.server.repositories.sql.sys.SystemBroadCastMsg;
import com.bin.im.server.repositories.sql.sys.SystemMsg;
import com.bin.im.server.spi.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.bin.im.server.common.type.MsgType.MSG_TYPE_BROCAST;
import static com.bin.im.server.common.type.MsgType.MSG_TYPE_SYS;

/**
 * 预加载指定联系人最近的消息
 */

@Service(name = "msg", module = "sys", type = ServiceType.DUBBO)
public class PreloadSysMsgHandler extends BaseHandler {


    public List<OnlineMsgInfo> loadSysMsg(PreloadSysMsg preloadSysMsg) {
        List<ContactPreloadSysMsg> contactPreloadSysMsgs = preloadSysMsg.getContactPreloadSysMsgs();
        long reqUid = preloadSysMsg.getReqUid();

        Map<Long, ContactPreloadSysMsg> contactsMap =
                contactPreloadSysMsgs.stream().collect(Collectors.toMap(ContactPreloadSysMsg::getContactUid, contacts -> contacts));

        List<ContactsAckMsgInfo> contactsAckMsgInfos =
                imDas().queryMarkAckMsgId(reqUid, List.copyOf(contactsMap.keySet())).getMsg();

        List<OnlineMsgInfo> allOnlineMsgs = new ArrayList<>();
        for (ContactsAckMsgInfo contactsAckMsgInfo : contactsAckMsgInfos) {
            long contactUid = contactsAckMsgInfo.getContactUid();
            ContactPreloadSysMsg currContact = contactsMap.get(contactUid);

            int msgCount = currContact.getMsgCount();
            long lastMsgId = currContact.getLastMsgId();
            if (lastMsgId <= 0) {
                lastMsgId = contactsAckMsgInfo.getLastMsgId();
            }

            QueryBaseMsgModel queryBaseMsg = new QueryBaseMsgModel();
            queryBaseMsg.setFromUid(contactUid);
            queryBaseMsg.setToUid(reqUid);
            queryBaseMsg.setCount(msgCount);
            queryBaseMsg.setLastMsgId(lastMsgId);
            List<SystemMsg> systemMsgs = imDas().querySysMsg(queryBaseMsg).getMsg();
            for (SystemMsg systemMsg : systemMsgs) {
                OnlineMsgInfo onlineMsg = new OnlineMsgInfo();
                onlineMsg.setFromUid(systemMsg.getFromUid());
                onlineMsg.setToUid(systemMsg.getToUid());
                onlineMsg.setTimestamp(systemMsg.getTimestamp());
                onlineMsg.setMsgId(systemMsg.getMsgId());
                onlineMsg.setMsgType(MSG_TYPE_SYS);
                onlineMsg.setClientMsgId(systemMsg.getTimestamp());
                onlineMsg.setMsgContent(systemMsg.getMsgContent());
                allOnlineMsgs.add(onlineMsg);
            }
        }
        return allOnlineMsgs;
    }


    public List<OnlineMsgInfo> loadSysBroadcastMsg(PreloadSysMsg preloadSysMsg) {
        long reqUid = preloadSysMsg.getReqUid();
        long currTime = TimeUtils.currTimeMillis();

        List<ContactPreloadSysMsg> contactPreloadSysMsgs =
                preloadSysMsg.getContactPreloadSysMsgs();

        Map<Long, ContactPreloadSysMsg> contactsMap =
                contactPreloadSysMsgs.stream().collect(Collectors.toMap(ContactPreloadSysMsg::getContactUid, contacts -> contacts));



        List<ContactsAckMsgInfo> contactsAckMsgInfos =
                imDas().queryMarkAckMsgId(reqUid, List.copyOf(contactsMap.keySet())).getMsg();


        BroadcastMsgMemoryHandler broadcastMsgMemory =
                getService(BroadcastMsgMemoryHandler.SERVICE_NAME);


        List<OnlineMsgInfo> allOnlineMsgs = new ArrayList<>();
        for (ContactsAckMsgInfo contactsAckMsgInfo : contactsAckMsgInfos) {
            long contactUid = contactsAckMsgInfo.getContactUid();
            ContactPreloadSysMsg currContact = contactsMap.get(contactUid);
            int msgCount = currContact.getMsgCount();


            long lastMsgId = currContact.getLastMsgId();
            if (lastMsgId <= 0) {
                lastMsgId = contactsAckMsgInfo.getLastMsgId();
            }

            List<SystemBroadCastMsg> systemBroadCastMsgs = imDas().querySystemBroadCastMsg(contactUid, lastMsgId, msgCount).getMsg();

            List<Long> msgIds = systemBroadCastMsgs.stream().map(SystemBroadCastMsg::getMsgId).collect(Collectors.toList());




            Map<Long, String> broadcastMsgMap = broadcastMsgMemory.findBroadcastMsgInMemory(msgIds);

            for (SystemBroadCastMsg systemBroadCastMsg : systemBroadCastMsgs) {
                long msgId = systemBroadCastMsg.getMsgId();

                long beginTs = systemBroadCastMsg.getBeginTs();
                long endTs = systemBroadCastMsg.getEndTs();


                if (currTime < beginTs) {   //未到生效时间，结束
                    continue;
                }
                if (currTime > endTs && endTs > 0 && endTs > beginTs) { //已经失效，清除，结束
                    BroadcastMsgCache.clearSysMsg(msgId);
                    continue;
                }

                OnlineMsgInfo onlineMsg = new OnlineMsgInfo();
                onlineMsg.setFromUid(systemBroadCastMsg.getSuid());
                onlineMsg.setToUid(reqUid);
                onlineMsg.setTimestamp(systemBroadCastMsg.getSendTime());
                onlineMsg.setMsgId(msgId);
                onlineMsg.setClientMsgId(systemBroadCastMsg.getSendTime());
                onlineMsg.setMsgType(MSG_TYPE_BROCAST);
                String msgContent = broadcastMsgMap.get(msgId);

                onlineMsg.setMsgContent(msgContent);
                allOnlineMsgs.add(onlineMsg);
            }

        }
        return allOnlineMsgs;

    }








}
