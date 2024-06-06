package com.bin.im.server.cache.redis;

import com.bin.im.server.cache.CacheInternal;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.Map;

public class RedisClusterCache implements CacheInternal {

    private JedisCluster jedis;

    public RedisClusterCache() {
        jedis = RedisClusterUtil.getJedis();

    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return jedis.hgetAll(key);
    }
    @Override
    public String hget(String key, String field) {
        return jedis.hget(key,field);
    }

    @Override
    public long del(String... key) {

        return jedis.del(key);
    }

    @Override
    public long hdel(String key, String... field) {
        return jedis.hdel(key,field);
    }

    @Override
    public long hset(String key, String field, String value) {
        return jedis.hset(key,field,value);
    }

    @Override
    public long hset(String key, Map<String, String> hash) {
        return jedis.hset(key,hash);
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public String setex(String key, String value,long seconds){
        return jedis.setex(key,seconds,value);
    }

    @Override
    public String setnxex(String key, String value, long seconds) {
        SetParams setParams = SetParams.setParams().ex(seconds).nx();
        return jedis.set(key, value, setParams);
    }

    @Override
    public long expire(String key, long seconds) {
        return jedis.expire(key,seconds);
    }

    @Override
    public boolean exists(String key) {
        return jedis.exists(key);
    }

    @Override
    public long incr(String key) {
        return jedis.incr(key);
    }

    @Override
    public void close() {
    }
}
