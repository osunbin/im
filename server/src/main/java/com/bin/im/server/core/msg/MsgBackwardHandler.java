package com.bin.im.server.core.msg;

import com.bin.im.server.domain.BackwardMsgNotify;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.domain.LoginInfo;
import com.bin.im.server.domain.MsgBackward;
import com.bin.im.server.repositories.sql.msg.OnlineMsg;
import com.bin.im.server.spi.annotation.Service;


import java.util.List;
import java.util.Map;

/**
 * 私信撤回
 */
@Service
public class MsgBackwardHandler extends BaseHandler {





    public void backwardMsg(MsgBackward msgBackward) {


        long fromUid = msgBackward.getFromUid();
        long toUid = msgBackward.getToUid();
        long clientMsgId = msgBackward.getClientMsgId();



        OnlineMsg onlineMsg = new OnlineMsg();
        onlineMsg.setToUid(toUid);
        onlineMsg.setFromUid(fromUid);
        onlineMsg.setClientMsgId(clientMsgId);

        imDas().backwardMsg(onlineMsg);
        onlineMsg.setToUid(fromUid);
        onlineMsg.setFromUid(toUid);
        imDas().backwardMsg(onlineMsg);







        Map<Long, LoginInfo> userLoginInfos = getUserInfoRouter().fetchUserLoginInfo(List.of(fromUid, toUid));

        // 是否修复发送者在线
        repairSendUserInfo(fromUid,userLoginInfos.get(fromUid));


        LoginInfo toLogin = userLoginInfos.get(toUid);
        if (toLogin == null) {
            return;
        }



        //推送私信撤回消息到接收者 TODO entry
        BackwardMsgNotify backwardMsgNotify = new BackwardMsgNotify();
        backwardMsgNotify.setFromUid(fromUid);
        backwardMsgNotify.setToUid(toUid);
        backwardMsgNotify.setClientMsgId(clientMsgId);

        MsgNotifyInfo msgNotifyInfo =
                MsgNotifyInfo.create(toUid, toLogin.getLoginESIp(), toLogin.getLoginESPort());


        getImEngine().notifyBackwardMsg(msgNotifyInfo,backwardMsgNotify);


    }


}
