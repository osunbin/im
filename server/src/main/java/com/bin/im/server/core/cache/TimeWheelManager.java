package com.bin.im.server.core.cache;

import com.bin.im.common.internal.utils.CollectionUtil;
import com.bin.im.common.mini.json.JsonObject;
import com.bin.im.common.internal.utils.StringUtils;
import com.bin.im.server.cache.CacheInternal;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.domain.MsgAckInfo;
import com.bin.im.server.spi.ImEngine;
import com.bin.im.server.spi.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通过redis实现时间轮，提供保存消息、处理ack、获取超时等关键功能
 */
@Service
public class TimeWheelManager extends BaseHandler {

    public static final String SERVICE_NAME = "TimeWheelManager";

    private static final String KEY_CURR_TIME = "key_curr_time";

    // TODO  可配置
    private int size = 15;

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    private CacheInternal getCache() {
        return imEngine.getCacheManager().getCache();
    }


    public void addMsg(MsgAckInfo info) {
        addMsg(info.getAckKey(),info.getJson());
    }

    //保存消息(先获取curr_time_key,再保存消息)
    public void addMsg(String msgId, String msgBody) {
        CacheInternal cache = getCache();

        String currTimeKey = getCurrTimeKey(cache);

        //保存k-v  msgid-timeKey
        cache.setex(msgId,currTimeKey,60);

        //保存hash  timeKey-msgid-msg_body
        cache.hset(currTimeKey,msgId,msgBody);

        cache.close();
    }


    public void batchDealAck(List<String> ackIds) {
        if (CollectionUtil.isEmpty(ackIds)) return;
        CacheInternal cache = getCache();

        for (String ackId : ackIds) {
            //获取 ackid 对应的timeKey
            String timeKey = cache.get(ackId);

            //删除hash  timeKey-msgId
            cache.hdel(timeKey,ackId);

            cache.del(ackId);
        }
        cache.close();
    }
    //处理ACK
    public void dealAck(String ackid) {
        CacheInternal cache = getCache();

        //获取 ackid 对应的timeKey
        String timeKey = cache.get(ackid);

        //删除hash  timeKey-msgId
        cache.hdel(timeKey,ackid);

        cache.del(ackid);

        cache.close();

    }

    public List<MsgAckInfo> getTimeOutMsgAckInfo(){
        List<String> msgAckInfoJsons = getTimeOutMsg();
        List<MsgAckInfo> msgAckInfos = new ArrayList<>();
        for (String json : msgAckInfoJsons) {
            msgAckInfos.add(MsgAckInfo.parseFromString(json));
        }
        return msgAckInfos;
    }

    //获取超时消息(先获取curr_time_key,
    // 然后获取next_curr_key对应的超时消息集合,
    // 再删除超时集合,最后设置next_time_key)
    public List<String> getTimeOutMsg() {

        CacheInternal cache = getCache();
        String currTimeKey = getCurrTimeKey(cache);


        //生成nextTimeKey
        if (StringUtils.isEmpty(currTimeKey)) {
            currTimeKey = "0"; //首次，从 0 开始 curr time
        }

        int currTime = Integer.parseInt(currTimeKey);


        int nextTime = (currTime + 1) % size;
        String nextTimeKey = String.valueOf(nextTime);

        //获取timeKey对应的所有msgId

        Map<String, String> allHash = cache.hgetAll(nextTimeKey);

        List<String> msgIds = new ArrayList<>();
        if (allHash != null) {
            Set<String> keys = allHash.keySet();
            for (String key : keys) {
                cache.del(key);
                msgIds.add(allHash.get(key));
            }
        }

        //删除缓存的用户登录信息
        cache.del(nextTimeKey);


        //设置新的 currTime
        setCurrTimeKey(cache,nextTimeKey);

        cache.close();

        return msgIds;
    }


    private String getCurrTimeKey(CacheInternal cache) {

        return cache.get(KEY_CURR_TIME);

    }

    private void setCurrTimeKey(CacheInternal cache, String timeKey) {

        cache.setex(KEY_CURR_TIME, timeKey, 86400);

    }
}
