package com.bin.im.server.core.user;

import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.KeepAliveSysMsg;


import static com.bin.im.server.event.EventService.KEEP_ALIVE_TOPIC;

/**
 *  处理心跳业务(logic接收entry转发的心跳包后，通过ammq通知给extlogic)
 */
public class KeepAliveHandler extends BaseHandler {


    public void keepAlive(BaseUserInfo baseUserInfo) {
        long uid = baseUserInfo.getUid();
        int sourceType = baseUserInfo.getSourceType();

        Message<KeepAliveSysMsg> message =
                Message.of(KEEP_ALIVE_TOPIC,new KeepAliveSysMsg(uid,sourceType));
        publishEvent(message);
        logger.info("user keepalive uid:{} sourceType:{}  esIp:{} "
                ,uid,sourceType,baseUserInfo.getEsIp());
    }


}
