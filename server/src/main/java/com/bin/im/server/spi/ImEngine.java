package com.bin.im.server.spi;

import com.bin.im.server.domain.SMSendMsgNotify;
import com.bin.im.server.domain.BackwardMsgNotify;
import com.bin.im.server.domain.MsgReadedNotify;
import com.bin.im.server.domain.SendMsgNotify;
import com.bin.im.server.cache.CacheManager;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.core.UserInfoRouter;
import com.bin.im.server.domain.KickoutUserNotify;
import com.bin.im.server.event.EventService;
import com.bin.im.server.event.Message;
import com.bin.im.server.repositories.ImDas;

import java.util.Collection;

public interface ImEngine {

    void notifyReadedMsg(MsgNotifyInfo msgNotifyInfo,MsgReadedNotify msgReadedNotify);

    void notifyBackwardMsg(MsgNotifyInfo msgNotifyInfo,BackwardMsgNotify backwardMsg);



    void notifyMsg(MsgNotifyInfo msgNotifyInfo, SMSendMsgNotify smSendMsgNotify);

    // 发消息 撤回消息  消息已读  关键的系统消息
    void notifyMsg(MsgNotifyInfo msgNotifyInfo, SendMsgNotify sendMsgNotify);

    void kickout(MsgNotifyInfo msgNotifyInfo,KickoutUserNotify kickoutUser);

    EventService getEventService();

    void publishEvent(Message<?> message);

    ImDas getImDas();

    long msgLock(String key,long seconds);

    CacheManager getCacheManager();

    UserInfoRouter getUserInfoRouter();

    <T> T getService(String serviceName);

    <S> Collection<S> getServices(Class<S> serviceClass);

}
