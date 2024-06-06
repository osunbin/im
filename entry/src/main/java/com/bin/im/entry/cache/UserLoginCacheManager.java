package com.bin.im.entry.cache;

import com.bin.im.entry.model.SocketUserLoginReq;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *  缓存用户登录过程中的对象
 */
public class UserLoginCacheManager {

    private ConcurrentMap<Long, SocketUserLoginReq> userLoginMap = new ConcurrentHashMap<>();
    private ConcurrentMap<Channel, SocketUserLoginReq> channelLoginMap = new ConcurrentHashMap<>();

    public static final UserLoginCacheManager userCache = new UserLoginCacheManager();


    public void addUserLoginReq(Long uid, SocketUserLoginReq loginReq) {
        this.userLoginMap.put(uid, loginReq);
    }

    public SocketUserLoginReq removeUserLoginReq(Long uid) {
        SocketUserLoginReq loginReq = this.userLoginMap.get(uid);
        if (loginReq == null) {
            return null;
        }

        return this.userLoginMap.remove(uid);
    }

    public SocketUserLoginReq getUserLoginReq(Long uid) {
        return this.userLoginMap.get(uid);
    }

    public void addChannelLoginReq(Channel channel, SocketUserLoginReq loginReq) {
        this.channelLoginMap.put(channel, loginReq);
    }

    public void removeChannelLoginReq(Channel channel) {
        SocketUserLoginReq loginReq = this.channelLoginMap.get(channel);
        if (loginReq == null) {
            return;
        }

        this.channelLoginMap.remove(channel);
    }

    public SocketUserLoginReq getUserLoginReq(Channel channel) {
        return this.channelLoginMap.get(channel);
    }
}
