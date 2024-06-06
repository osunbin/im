package com.bin.im.server.core.user;

import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.event.Message;
import com.bin.im.server.event.model.LoginChange;


import static com.bin.im.server.event.EventService.LOGIN_CHANGE_PUSH_TOPIC;

/**
 *  实现用户登出系统业务
 *  (logic接收entry登出请求后，同步清理redis数据，异步记录登出时间）
 */
public class UserLogoutHandler extends BaseHandler {



    public void logout(BaseUserInfo baseUserInfo) {
        long uid = baseUserInfo.getUid();
        int sourceType = baseUserInfo.getSourceType();
        String esIp = baseUserInfo.getEsIp();
        long currTime = System.currentTimeMillis();


        logger.info("user logout uid:{} source:{} esIp:{} loginTime:{}  logoutTime:{} ",
                uid,sourceType,esIp,baseUserInfo.getLoginTime(),currTime);


        //清理缓存的用户登录信息
        getUserInfoRouter().delUserLoginInfo(uid,sourceType);

        LoginChange loginChange =
                LoginChange.ofLogout(uid, sourceType, currTime);

        Message<LoginChange> message =
                Message.of(LOGIN_CHANGE_PUSH_TOPIC,loginChange);

        publishEvent(message);
        logger.info("publish logout   {} ",loginChange);
    }

}
