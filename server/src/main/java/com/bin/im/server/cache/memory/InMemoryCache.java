package com.bin.im.server.cache.memory;

import com.bin.im.common.internal.utils.MapUtil;
import com.bin.im.server.cache.CacheInternal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryCache implements CacheInternal {

    private ConcurrentMap<String, ConcurrentMap<String, String>> map =
            new ConcurrentHashMap<>();

    private ConcurrentMap<String, String> kv = new ConcurrentHashMap<>();

    private ConcurrentMap<String, Long> ttl = new ConcurrentHashMap<>();

    private ConcurrentMap<String, AtomicInteger> count = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> hgetAll(String key) {
        if (isExpire(key)) {
            return new HashMap<>();
        }
        return map.get(key);
    }

    @Override
    public String hget(String key, String field) {
        if (isExpire(key)) {
            return "";
        }
       return map.get(key).get(field);
    }

    @Override
    public long del(String... keys) {
        for (String key : List.of(keys)) {
            map.remove(key);
        }
        return 1;
    }

    @Override
    public long hdel(String key, String... fields) {
        ConcurrentMap<String, String> value = map.get(key);
        if (value == null)
            return 0;
        for (String field : List.of(fields)) {
            value.remove(field);
        }
        return 0;
    }

    @Override
    public long hset(String key, String field, String value) {
        ConcurrentMap<String, String> currKey = map.getOrDefault(key, new ConcurrentHashMap<>());

        currKey.put(field, value);
        return 1;
    }

    @Override
    public long hset(String key, Map<String, String> hash) {
        ConcurrentMap<String, String> currKey = map.getOrDefault(key, new ConcurrentHashMap<>());
        currKey.putAll(hash);
        return 1;
    }

    @Override
    public String get(String key) {
        if (isExpire(key)) {
            return "";
        }
        return kv.get(key);
    }

    @Override
    public String setex(String key, String value, long seconds) {
        Long ttlSeconds = ttl.getOrDefault(key, System.currentTimeMillis() / 1000);
        ttl.put(key, ttlSeconds + seconds);
        return kv.put(key, value);
    }

    @Override
    public String setnxex(String key, String value,long seconds){
        return setex(key,value,seconds);
    }

    @Override
    public long expire(String key, long seconds) {
        Long ttlSeconds = ttl.getOrDefault(key, System.currentTimeMillis() / 1000);
        ttl.put(key, ttlSeconds + seconds);
        return 1;
    }

    @Override
    public long incr(String key) {
        AtomicInteger c = count.getOrDefault(key, new AtomicInteger());
        return c.incrementAndGet();
    }

    @Override
    public boolean exists(String key) {
        return isExpire(key);
    }

    private boolean isExpire(String key) {
        long ttlSeconds = ttl.get(key);
        long sec = System.currentTimeMillis() / 1000;

        if (ttlSeconds > 0 && ttlSeconds < sec) {
            kv.remove(key);
            ttl.remove(key);
            return true;
        }
        return  false;
    }

    @Override
    public void close() {

    }
}
