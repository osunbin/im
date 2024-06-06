package com.bin.im.server.core.sm;

import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.server.domain.SMSendMsgNotify;
import com.bin.im.server.domain.SmSendMsgAckModel;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.core.cache.TimeWheelManager;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.domain.SmSendMsg;

import com.bin.im.server.repositories.sql.sm.SmOnlineMsg;
import com.bin.im.server.spi.annotation.Service;


import java.util.List;
import java.util.Map;




/**
 * 子母账号业务——发送私信
 */
@Service
public class SmMsgSendHandler extends BaseHandler {


    /**
     * 商户侧业务
     * 用户uid=123
     * 母号uid=100
     * 子号uid=101
     * when user 123 send msg to child 101, the msg record like this:
     * msg_id   uidA     uidB      uidC     uidC_type    from_uid
     * 11111     123      100      101          0           1
     * 22222     101      123      100          1           0
     */
    public SmSendMsgAckModel sendSmMsg(SmSendMsg smSendMsg) {
        long muid = smSendMsg.getMuid();
        long userUid = smSendMsg.getUserUid();
        // 用户发送给商家  需要分配 子号 suid  开始聊天时就分配好 那个子号
        if (smSendMsg.isFromUser() && smSendMsg.getSuid() == 0) {
            long suid = 1L;
        }
        long suid = smSendMsg.getSuid();
        long fromUid = suid;
        long toUid = userUid;
        if (smSendMsg.isFromUser()) {
            fromUid = userUid;
            toUid = suid;
        }


        long currTime = TimeUtils.currTimeMillis();

        Map<Long, LoginInfo> loginInfoMap = getUserInfoRouter().fetchUserLoginInfo(List.of(fromUid, toUid));
        repairSendUserInfo(fromUid, loginInfoMap.get(fromUid));

        boolean toUserOnline = true;
        LoginInfo loginInfo = loginInfoMap.get(toUid);
        if (loginInfo == null) {
            toUserOnline = false;
        }
        SmOnlineMsg smOnlineMsg = new SmOnlineMsg();
        smOnlineMsg.setMsgType(smSendMsg.getMsgType());
        smOnlineMsg.setTimestamp(currTime);
        smOnlineMsg.setClientMsgId(smSendMsg.getClientMsgId());
        smOnlineMsg.setInfoId(smSendMsg.getInfoId());
        smOnlineMsg.setMsgContent(smSendMsg.getMsgContent());


        if (smSendMsg.isFromUser()) {
            smOnlineMsg.setFromUid(userUid);
            smOnlineMsg.setToUid(muid);
            smOnlineMsg.setSmUid(suid);
            smOnlineMsg.setUidSm(false);
            smOnlineMsg.setDirection(1);
        } else {
            smOnlineMsg.setFromUid(suid);
            smOnlineMsg.setToUid(userUid);
            smOnlineMsg.setSmUid(muid);
            smOnlineMsg.setUidSm(true);
            smOnlineMsg.setDirection(1);
        }
        long msgIdA = imDas().saveOnlineMsg(smOnlineMsg).getMsg();

        if (smSendMsg.isFromUser()) {
            smOnlineMsg.setFromUid(suid);
            smOnlineMsg.setToUid(userUid);
            smOnlineMsg.setSmUid(muid);
            smOnlineMsg.setUidSm(true);
            smOnlineMsg.setDirection(0);
        } else {
            smOnlineMsg.setFromUid(userUid);
            smOnlineMsg.setToUid(muid);
            smOnlineMsg.setSmUid(suid);
            smOnlineMsg.setUidSm(false);
            smOnlineMsg.setDirection(0);
        }
        long msgIdB = imDas().saveOnlineMsg(smOnlineMsg).getMsg();

        // TODO  更新联系人 lastMsgId

        if (toUserOnline) {
            SMSendMsgNotify smSendMsgNotify = new SMSendMsgNotify();
            smSendMsgNotify.setUserUid(smSendMsg.getUserUid());
            smSendMsgNotify.setMuid(smSendMsg.getMuid());
            smSendMsgNotify.setSuid(smSendMsg.getSuid());
            smSendMsgNotify.setFromUser(smSendMsg.isFromUser());
            smSendMsgNotify.setMsgTime(currTime);
            smSendMsgNotify.setClientMsgId(smSendMsg.getClientMsgId());
            smSendMsgNotify.setMsgContent(smSendMsg.getMsgContent());
            smSendMsgNotify.setMsgType(smSendMsg.getMsgType());
            smSendMsgNotify.setMsgId(msgIdB);
            smSendMsgNotify.setNoticeMsg(smSendMsg.getNoticeMsg());


            MsgNotifyInfo msgNotifyInfo =
                    MsgNotifyInfo.create(toUid, loginInfo.getLoginESIp(), loginInfo.getLoginESPort());

            getImEngine().notifyMsg(msgNotifyInfo,smSendMsgNotify);

            MsgAckInfo pAckInfo = new MsgAckInfo();
            pAckInfo.setFromUid(fromUid);
            pAckInfo.setToUid(toUid);
            pAckInfo.setMsgType(smSendMsg.getMsgType());
            pAckInfo.setSourceType(smSendMsg.getSourceType());
            pAckInfo.setClientMsgId(smSendMsg.getClientMsgId());
            pAckInfo.setMsgId(msgIdB);
            pAckInfo.setTimestamp(currTime / 1000);
            pAckInfo.setMsgContent(smSendMsg.getMsgContent());
            TimeWheelManager timeWheel =
                    getService(TimeWheelManager.SERVICE_NAME);
            timeWheel.addMsg(pAckInfo);
        }


        SmSendMsgAckModel smSendMsgAck = new SmSendMsgAckModel();
        smSendMsgAck.setMsgTime(currTime);
        smSendMsgAck.setMsgId(msgIdA);

        return smSendMsgAck;
    }

}
