package com.bin.im.server.core.msg;

import com.bin.im.common.internal.utils.CollectionUtil;
import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.server.domain.MsgReadedNotify;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.core.cache.TimeWheelManager;
import com.bin.im.server.domain.BatchMsgAck;
import com.bin.im.server.domain.ContactsReadedInfo;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.domain.MsgAck;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.domain.MsgReaded;
import com.bin.im.server.domain.MsgReadedAck;
import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.spi.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *  消息确认
 */
@Service
public class MsgAckHandler extends BaseHandler {

    // p2p
    public void msgAck(BatchMsgAck batchMsgAck) {
        List<MsgAck> msgAcks = batchMsgAck.getMsgAcks();
        if (CollectionUtil.isEmpty(msgAcks)) return;
        long toUid = batchMsgAck.getToUid();
        TimeWheelManager timeWheel = getService(TimeWheelManager.SERVICE_NAME);
        List<String> ackKeys = new ArrayList<>();
        List<UserContacts> multiUserContacts = new ArrayList<>();
        long lastMsgId = 0;
        long currTimeMillis = TimeUtils.currTimeMillis();
        MsgAckInfo ackInfo = new MsgAckInfo();
        ackInfo.setToUid(toUid);

        for (MsgAck msgAck : msgAcks) {
            long fromUid = msgAck.getFromUid();

            ackInfo.setFromUid(fromUid);

            List<Long> msgIds = msgAck.getMsgIds();
            for (long msgId : msgIds) {

                ackInfo.setMsgId(msgId);
                String ackKey = ackInfo.getAckKey();
                ackKeys.add(ackKey);
                lastMsgId = Math.max(lastMsgId,msgId);
            }

            UserContacts userContacts = new UserContacts();
            userContacts.setUidA(toUid);  // 接收方
            userContacts.setUidB(fromUid);
            // 在线 修改 3 个
            userContacts.setLastAckMsgId(lastMsgId);
            userContacts.setReadTime(currTimeMillis);
            // 离线只修改 lastTime
            userContacts.setLastTime(currTimeMillis);
            multiUserContacts.add(userContacts);



        }
        timeWheel.batchDealAck(ackKeys);
        imDas().batchUpdateOnlineSingleContactReceive(multiUserContacts);

        List<Long> fromUids = msgAcks.stream().map(ack -> ack.getFromUid()).collect(Collectors.toList());

        Map<Long, LoginInfo> loginInfoMap = getUserInfoRouter().fetchUserLoginInfo(fromUids);
        for (long fromUid : fromUids) {
            LoginInfo loginInfo = loginInfoMap.get(fromUid);
            if (loginInfo == null) continue;

            MsgReadedNotify msgReadedNotify = new MsgReadedNotify(fromUid,currTimeMillis);
            MsgNotifyInfo msgNotifyInfo =
                    MsgNotifyInfo.create(fromUid, loginInfo.getLoginESIp(), loginInfo.getLoginESPort());

            getImEngine().notifyReadedMsg(msgNotifyInfo,msgReadedNotify);
        }



    }


    /**
     * 离线后上线 拉取消息已读时间  默认 查看消息最终时间
     */
    public MsgReadedAck fetchMsgReaded(MsgReaded msgReaded) {

        long reqUid = msgReaded.getReqUid();
        List<Long> toUids = msgReaded.getToUids();
        if (CollectionUtil.isEmpty(toUids)) return null;

        List<ContactsReadedInfo> readTimeInfo = imDas().queryMarkReadMsg(reqUid, toUids).getMsg();


        return new MsgReadedAck(readTimeInfo);
    }
}
