package com.bin.im.server.core.msg;

import com.bin.im.common.internal.utils.TimeUtils;
import com.bin.im.server.domain.SendMsgAckModel;
import com.bin.im.server.domain.SendMsgNotify;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.core.cache.TimeWheelManager;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.domain.SendMsgInfo;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.OfflineMsgData;
import com.bin.im.server.event.model.OnlineMsgData;

import com.bin.im.server.repositories.sql.contacts.UserContacts;
import com.bin.im.server.repositories.sql.msg.OnlineMsg;
import com.bin.im.server.spi.annotation.Service;
import com.bin.im.server.spi.impl.ServerContext;
import com.bin.im.server.spi.impl.ServiceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.bin.im.server.event.EventService.OFFLINE_PUSH_TOPIC;
import static com.bin.im.server.event.EventService.ONLINE_MSG_TOPIC;



@SuppressWarnings("all")
@Service
public class MsgSendHandler extends BaseHandler {

    /**
     * 消息去重 cliemtMsgId  去重维度-fromUid+toUid+cliemtMsgId、fromUid+cliemtMsgId、cliemtMsgId
     */
    public SendMsgAckModel sendMsg(SendMsgInfo sendMsgInfo) {
        long fromUid = sendMsgInfo.getFromUid();
        long toUid = sendMsgInfo.getToUid();
        if (toUid == 0 || toUid == fromUid) {  //消息接收方uid不可能是0  禁止用户给自己发消息
            return null;
        }
        String msgContent = sendMsgInfo.getMsgContent();

        //
        antiSpamCheck(sendMsgInfo);

        long currTime = TimeUtils.currTimeMillis();

        Map<Long, LoginInfo> longLoginInfoMap = getUserInfoRouter().fetchUserLoginInfo(List.of(fromUid, toUid));

        repairSendUserInfo(fromUid, longLoginInfoMap.get(fromUid));
        boolean toUserOnline = false;
        LoginInfo toUserLoginInfo = longLoginInfoMap.get(toUid);
        if (toUserLoginInfo != null) {
            toUserOnline = true;
        }



        logger.info("textMsg fromUid:{}  toUid:{} text:{} ", fromUid, toUid, msgContent);



        OnlineMsg onlineMsgnew = new OnlineMsg();
        onlineMsgnew.setFromUid(fromUid);
        onlineMsgnew.setToUid(toUid);
        onlineMsgnew.setMsgType(sendMsgInfo.getMsgType());
        onlineMsgnew.setDirection(1);
        onlineMsgnew.setTimestamp(currTime);
        onlineMsgnew.setClientMsgId(sendMsgInfo.getClientMsgId());
        onlineMsgnew.setMsgContent(msgContent);
        long msgIdA = imDas().saveOnlineMsg(onlineMsgnew).getMsg();

        onlineMsgnew.setFromUid(toUid);
        onlineMsgnew.setToUid(fromUid);
        onlineMsgnew.setDirection(0);
        long msgIdB = imDas().saveOnlineMsg(onlineMsgnew).getMsg();



        UserContacts userContacts = new UserContacts();
        userContacts.setUidA(fromUid);
        userContacts.setUidB(toUid);
        userContacts.setLastAckMsgId(msgIdA);
        userContacts.setLastTime(currTime);
        imDas().updateSingleContactSended(userContacts);





        if (toUserOnline) {

            SendMsgNotify sendMsgNotify = new SendMsgNotify();
            sendMsgNotify.setFromUid(fromUid);
            sendMsgNotify.setToUid(toUid);
            sendMsgNotify.setTime(currTime);
            sendMsgNotify.setClientMsgId(sendMsgInfo.getClientMsgId());
            sendMsgNotify.setMsgContent(msgContent);
            sendMsgNotify.setMsgType(sendMsgInfo.getMsgType());
            sendMsgNotify.setMsgId(msgIdA);
            sendMsgNotify.setNoticeMsg(sendMsgInfo.getNoticeMsg());


            MsgNotifyInfo msgNotifyInfo =
                    MsgNotifyInfo.create(toUid, toUserLoginInfo.getLoginESIp(), toUserLoginInfo.getLoginESPort());

            getImEngine().notifyMsg(msgNotifyInfo,sendMsgNotify);


            MsgAckInfo pAckInfo = new MsgAckInfo();
            pAckInfo.setFromUid(fromUid);
            pAckInfo.setToUid(toUid);
            pAckInfo.setMsgType(sendMsgInfo.getMsgType());
            pAckInfo.setSourceType(sendMsgInfo.getSourceType());
            pAckInfo.setClientMsgId(sendMsgInfo.getClientMsgId());
            pAckInfo.setMsgId(msgIdA);
            pAckInfo.setTimestamp(currTime / 1000);
            pAckInfo.setMsgContent(msgContent);

            TimeWheelManager timeWheel = getImEngine().getService(TimeWheelManager.SERVICE_NAME);

            timeWheel.addMsg(pAckInfo);


        } else {

            userContacts.setUidA(toUid);
            userContacts.setUidB(fromUid);
            userContacts.setUnreadCount(1);
            imDas().updateOfflineSingleContactReceive(userContacts);


            OfflineMsgData offlineMsg = new OfflineMsgData();
            offlineMsg.setFromUid(fromUid);
            offlineMsg.setToUid(toUid);
            offlineMsg.setFromSource(sendMsgInfo.getSourceType());
            offlineMsg.setMsgType(sendMsgInfo.getMsgType());
            offlineMsg.setClientMsgId(sendMsgInfo.getClientMsgId());
            offlineMsg.setMsgId(msgIdB);
            offlineMsg.setMsgContent(msgContent);
            publishEvent(Message.of(OFFLINE_PUSH_TOPIC, offlineMsg));
        }

        publishOnlineMsgEvent(sendMsgInfo,toUserOnline,msgIdA,msgIdB,currTime);

        SendMsgAckModel sendMsgAckModel = new SendMsgAckModel();
        sendMsgAckModel.setMsgId(msgIdB);
        sendMsgAckModel.setTimestamp(currTime);
        // sendMsgAckModel.setMessage("spam 风险提示");
        return sendMsgAckModel;
    }


    private void publishOnlineMsgEvent(SendMsgInfo sendMsgInfo,boolean toUserOnline,long msgIdA,long msgIdB,long currTime) {
        OnlineMsgData onlineMsgData = new OnlineMsgData();
        onlineMsgData.setToUidMsgId(msgIdA);
        onlineMsgData.setFromUidMsgId(msgIdB);
        onlineMsgData.setClientMsgId(sendMsgInfo.getClientMsgId());
        onlineMsgData.setToUid(sendMsgInfo.getToUid());
        onlineMsgData.setFromUid(sendMsgInfo.getFromUid());
        onlineMsgData.setMsgType(sendMsgInfo.getMsgType());
        onlineMsgData.setMsgSendTime(currTime);
        onlineMsgData.setMsgContent(sendMsgInfo.getMsgContent());
        onlineMsgData.setFromSource(sendMsgInfo.getSourceType());
        onlineMsgData.setNoticeMsg(sendMsgInfo.getNoticeMsg());
        onlineMsgData.setToUserIsOnline(toUserOnline ? 1 : 0);

        Message<OnlineMsgData> onlineMsg =
                Message.of(ONLINE_MSG_TOPIC, onlineMsgData);
        onlineMsg.setTag("insert_online_msg");
        publishEvent(onlineMsg);
    }

    private void antiSpamCheck(SendMsgInfo sendMsgInfo) {
        String msgContent = sendMsgInfo.getMsgContent();

        ServerContext currContext = ServiceManager.getCurrContext();
        String clientIp = currContext.getClientIp();

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("uid", sendMsgInfo.getFromUid());
        jsonObject.put("ip", clientIp);
        jsonObject.put("toUid", sendMsgInfo.getToUid());
        jsonObject.put("sourceType", sendMsgInfo.getSourceType());
        jsonObject.put("clientMsgId", sendMsgInfo.getClientMsgId());
        jsonObject.put("msgType", sendMsgInfo.getMsgType());
        jsonObject.put("msgContent", sendMsgInfo.getMsgContent());
        jsonObject.put("noticeMsg", sendMsgInfo.getNoticeMsg());

        jsonObject.put("url","");
        jsonObject.put("md5","");
        jsonObject.put("content", "text");

        jsonObject.put("phash", "图片指纹");
        jsonObject.put("longitude", "经度");
        jsonObject.put("latitude", "纬度");

    }







}
