package com.bin.im.entry.cache;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class RequestSessionCacheManager {

    private static final ConcurrentMap<Long, Channel> requestSessionMap = new ConcurrentHashMap<>();

    private static AtomicLong reqSession = new AtomicLong();

    public static final RequestSessionCacheManager sessionCache = new RequestSessionCacheManager();


    public  long createSession() {
       return reqSession.getAndIncrement();
    }
    public  void addChannel(long session,Channel channel) {
        requestSessionMap.put(session,channel);
    }

    public  Channel getChannel(long session) {
       return requestSessionMap.get(session);
    }



}
