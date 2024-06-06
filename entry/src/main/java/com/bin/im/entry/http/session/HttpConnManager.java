package com.bin.im.entry.http.session;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpConnManager {

    private Map<Long, Channel> reqConn = new ConcurrentHashMap<>();


    public void saveConn(long seq,Channel channel) {
        Channel already = reqConn.get(seq);
        if (already != null) {
            return; // already exist
        }
        reqConn.put(seq,channel);
    }


    public void removeConn(long seq,Channel channel) {
        reqConn.remove(seq);
    }


    public void sendOk(Channel channel,String data) {
        channel.writeAndFlush(data);
    }

    public void sendFail(Channel channel,int code,String data) {
        channel.writeAndFlush(data);
    }




}
