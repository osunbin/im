package com.bin.im.server.spi.impl;

import com.bin.im.server.domain.BackwardMsgNotify;
import com.bin.im.server.domain.MsgReadedNotify;
import com.bin.im.server.domain.SendMsgNotify;
import com.bin.im.server.cache.CacheInternal;
import com.bin.im.server.cache.CacheManager;
import com.bin.im.server.core.MsgNotifyInfo;
import com.bin.im.server.core.UserInfoRouter;
import com.bin.im.server.domain.KickoutUserNotify;
import com.bin.im.server.event.EventService;
import com.bin.im.server.event.EventServiceImpl;
import com.bin.im.server.event.Message;
import com.bin.im.server.repositories.ImDas;
import com.bin.im.server.repositories.ImDasImpl;
import com.bin.im.server.repositories.config.SqlDbConfig;
import com.bin.im.server.spi.ImEngine;
import com.bin.im.server.spi.invoker.InvokerManager;
import org.jdbi.v3.core.Jdbi;

import java.util.Collection;
import java.util.Map;

public class ImEngineImpl implements ImEngine {


    private final ServiceManagerImpl serviceManager;
    private final EventServiceImpl eventService;
    private final ImDas imDas;
    private final CacheManager cacheManager;
    private Jdbi jdbi;
    private InvokerManager invokerManager;
    private UserInfoRouter userInfoRouter;

    public ImEngineImpl() {
        this.jdbi = SqlDbConfig.jdbi();
        this.serviceManager = new ServiceManagerImpl(this);
        eventService = new EventServiceImpl(this);

        imDas = new ImDasImpl(this);
        cacheManager = new CacheManager(this);
        invokerManager = new InvokerManager();
        userInfoRouter = new UserInfoRouter(this);
    }


    public void start() {
        serviceManager.start();
    }

    public void notifyReadedMsg(MsgNotifyInfo msgNotifyInfo, MsgReadedNotify msgReadedNotify) {
        notifyEntry(msgNotifyInfo, msgReadedNotify.toMap());
    }

    public void notifyBackwardMsg(MsgNotifyInfo msgNotifyInfo, BackwardMsgNotify backwardMsg) {
        notifyEntry(msgNotifyInfo, backwardMsg.toMap());
    }


    public void notifyMsg(MsgNotifyInfo msgNotifyInfo, SendMsgNotify sendMsgNotify) {
        notifyEntry(msgNotifyInfo, sendMsgNotify.toMap());
    }


    public void kickout(MsgNotifyInfo msgNotifyInfo, KickoutUserNotify kickoutUser) {
        notifyEntry(msgNotifyInfo, kickoutUser.toMap());
    }


    public void notifyEntry(MsgNotifyInfo msgNotifyInfo, Map<String, Object> parameterMap) {
        invokerManager.notifyEntry(msgNotifyInfo, parameterMap);
    }


    public UserInfoRouter getUserInfoRouter() {
        return userInfoRouter;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void publishEvent(Message<?> message) {
        eventService.publishEvent(message);
    }

    public ImDas getImDas() {
        return imDas;
    }

    public long msgLock(String key, long seconds) {
        long count = 0;
        try (CacheInternal cache = getCacheManager().getCache()) {
            count = cache.incr(key);
            cache.expire(key, 7200);
        }
        return count;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }


    public Jdbi getJdbi() {
        return jdbi;
    }

    @Override
    public <T> T getService(String serviceName) {
        T service = serviceManager.getService(serviceName);
        return service;
    }

    @Override
    public <S> Collection<S> getServices(Class<S> serviceClass) {
        return serviceManager.getServices(serviceClass);
    }


    public void shutdown(boolean terminate) {

    }
}
