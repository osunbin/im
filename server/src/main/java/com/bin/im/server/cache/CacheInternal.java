package com.bin.im.server.cache;

import java.io.Closeable;
import java.util.Map;

public interface CacheInternal extends Closeable {

    Map<String, String> hgetAll(String key);

    String hget(String key, String field);

    long del(String... key);


    long hdel(String key, String... field);

    long hset(final String key, final String field, final String value);

    long hset(final String key, Map<String,String> hash);



    String get(String key);

    String setex(String key, String value,long seconds);

    String setnxex(String key, String value,long seconds);

    long expire(String key, long seconds);

    boolean exists(final String key);

    long incr(String key);

    void close();
}
