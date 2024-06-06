package com.bin.im.server.core.cache;


import com.bin.im.common.internal.utils.BiTuple;
import com.bin.im.common.internal.utils.StringUtils;
import com.bin.im.server.repositories.model.sys.BroadCastSysMsgContentModel;
import com.bin.im.server.repositories.sql.sys.SystemMsgContent;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

public class BroadcastMsgCache {



    private static final ConcurrentMap<Long, String> sysMsgMap =
            new ConcurrentHashMap<>();


    private static final ConcurrentMap<Long, BroadCastSysMsgContentModel> msgMap =
            new ConcurrentHashMap<>();

    private static final  Cache<Long, String> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();



    public static BroadCastSysMsgContentModel getSysMsg(long msgId) {
        return msgMap.get(msgId);
    }


    public static void addNewSysMsg(BroadCastSysMsgContentModel sysMsg) {
        msgMap.put(sysMsg.getMsgId(), sysMsg);
    }

    public static void delSysMsg(long msgId) {
        msgMap.remove(msgId);
    }




    public static String getSysBroadcastMsg(long msgId) {
        return cache.getIfPresent(msgId);
    }

    public static void addNewSysMsg(long msgId,String msgContent) {
        cache.put(msgId, msgContent);
    }



    public static void clearSysMsg(long msgId) {
        cache.invalidate(msgId);
    }



    public static BiTuple<Map<Long, String>, List<Long>> getSysBroadcastMsg(List<Long> msgIds) {

        List<Long> empty = new ArrayList<>();
        Map<Long, String> data = new HashMap<>();
        for (long msgId : msgIds) {
            String value = cache.getIfPresent(msgId);
            if (StringUtils.isEmpty(value)) {
                empty.add(msgId);
            } else {
                data.put(msgId, value);
            }
        }
        return BiTuple.of(data, empty);
    }

}
