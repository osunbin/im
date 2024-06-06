package com.bin.im.server.cache;

import com.bin.im.server.cache.memory.InMemoryCache;
import com.bin.im.server.cache.redis.RedisPoolCache;
import com.bin.im.server.spi.ImEngine;

public class CacheManager {

    private CacheInternal cache;
    private ImEngine imEngine;
    public CacheManager(ImEngine imEngine) {
        this.imEngine = imEngine;
        cache = new InMemoryCache();
    }

    public  CacheInternal getCache() {
        if (cache instanceof RedisPoolCache) {
            ((RedisPoolCache)cache).init();
        }
        return cache;
    }


}
