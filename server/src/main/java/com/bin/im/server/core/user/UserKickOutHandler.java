package com.bin.im.server.core.user;


import com.bin.im.server.core.BaseHandler;


/**
 *  实现踢人业务（entry心跳检测超时后，发送kickout通知包到logic，logic清理内存数据）
 */
public class UserKickOutHandler extends BaseHandler {




    public void kickout(BaseUserInfo baseUserInfo) {
        long currTime = System.currentTimeMillis();
        long uid = baseUserInfo.getUid();
        int sourceType = baseUserInfo.getSourceType();

        getUserInfoRouter().delUserLoginInfo(uid,sourceType);

        logger.info("user  kickout uid:{} heartdead kickouttime:{} sourceType:{} ",uid,currTime,sourceType);
    }
}
