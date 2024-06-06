package com.bin.im.server.core.sys;


import com.bin.im.server.domain.SendMsgNotify;
import com.bin.im.server.domain.SysGroupMsgAckModel;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.core.cache.TimeWheelManager;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.domain.QuerySysMsg;
import com.bin.im.server.domain.SysGroupMsg;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.OfflineMsgData;
import com.bin.im.server.repositories.model.QueryBaseMsgModel;
import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.repositories.sql.sys.SystemMsg;
import com.bin.im.server.spi.annotation.Service;


import java.util.List;

import static com.bin.im.server.common.type.MsgType.MSG_TYPE_SYS;


/**
 *  获取系统消息
 */
@Service
public class SysMsgHandler extends BaseHandler {

    /**
     * 1)联系人会记录 用户 与 系统联系人 ack 的最后一条,登录时根据 lastAckMsgid 拉取新消息
     * 2) 也可以根据msgid 拉取联系人
     */
    public List<SystemMsg> fetchSysMsg(QuerySysMsg querySysMsg) {
        long reqUid = querySysMsg.getReqUid();
        long sysContactId = querySysMsg.getSysContactId(); // TODO 系统默认配置/前端传送
        int msgCount = querySysMsg.getMsgCount();
        QueryBaseMsgModel queryBaseMsg = new QueryBaseMsgModel();
        queryBaseMsg.setFromUid(sysContactId);
        queryBaseMsg.setToUid(reqUid);
        queryBaseMsg.setCount(msgCount);
        queryBaseMsg.setLastMsgId(querySysMsg.getLastMsgId());
        List<SystemMsg> systemMsgs = imDas().querySysMsg(queryBaseMsg).getMsg();
        return systemMsgs;
    }



    public SysGroupMsgAckModel sendSysMsg(SysGroupMsg sysGroupMsg) {
        long sysUid = sysGroupMsg.getSysUid();
        long toUid = sysGroupMsg.getToUid();

        boolean  toUidIsOnline = false;


        LoginInfo loginInfo = getUserInfoRouter().fetchUserLoginInfo(toUid);
        if (loginInfo == null) {
            toUidIsOnline = true;
        }

        long msgSendTime = System.currentTimeMillis();

        SystemMsg systemMsg = new SystemMsg();
        systemMsg.setToUid(toUid);
        systemMsg.setFromUid(sysUid);
        systemMsg.setMsgType(MSG_TYPE_SYS);
        systemMsg.setMsgContent(sysGroupMsg.getMsgContent());
        systemMsg.setTimestamp(msgSendTime);


        long msgId = imDas().saveSysMsg(systemMsg).getMsg();


        if (toUidIsOnline){
            SendMsgNotify sendMsgNotify = new SendMsgNotify();
            sendMsgNotify.setFromUid(sysUid);
            sendMsgNotify.setToUid(toUid);
            sendMsgNotify.setTime(msgSendTime);
            sendMsgNotify.setMsgContent(sysGroupMsg.getMsgContent());
            sendMsgNotify.setMsgType(MSG_TYPE_SYS);
            sendMsgNotify.setMsgId(msgId);

            MsgNotifyInfo msgNotifyInfo =
                    MsgNotifyInfo.create(toUid, loginInfo.getLoginESIp(), loginInfo.getLoginESPort());

            getImEngine().notifyMsg(msgNotifyInfo,sendMsgNotify);

            MsgAckInfo pAckInfo = new MsgAckInfo();
            pAckInfo.setFromUid(sysUid);
            pAckInfo.setToUid(toUid);
            pAckInfo.setMsgType(MSG_TYPE_SYS);
            pAckInfo.setSourceType(sysGroupMsg.getSourceType());
            pAckInfo.setMsgId(msgId);
            pAckInfo.setTimestamp(msgSendTime/1000);
            pAckInfo.setMsgContent(sysGroupMsg.getMsgContent());

            TimeWheelManager timeWheel =
                    getService(TimeWheelManager.SERVICE_NAME);
            timeWheel.addMsg(pAckInfo);

        } else {

            UserContacts userContacts = new UserContacts();
            userContacts.setUidA(toUid);
            userContacts.setUidB(sysUid);
            userContacts.setUnreadCount(1);
            userContacts.setLastTime(msgSendTime);

            imDas().updateOfflineSingleContactReceive(userContacts);


            OfflineMsgData offlineMsg = new OfflineMsgData();
            offlineMsg.setFromUid(sysUid);
            offlineMsg.setToUid(toUid);
            offlineMsg.setFromSource(sysGroupMsg.getSourceType());
            offlineMsg.setMsgContent(sysGroupMsg.getMsgContent());
            offlineMsg.setMsgType(MSG_TYPE_SYS);
            offlineMsg.setMsgId(msgId);

            publishOfflineEvent(Message.of(offlineMsg));

        }

        return new SysGroupMsgAckModel(msgSendTime,msgId);
    }




}
