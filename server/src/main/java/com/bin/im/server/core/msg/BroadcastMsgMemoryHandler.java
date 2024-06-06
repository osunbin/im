package com.bin.im.server.core.msg;

import com.bin.im.common.internal.utils.BiTuple;
import com.bin.im.common.internal.utils.CollectionUtil;
import com.bin.im.server.core.BaseHandler;
import com.bin.im.server.core.cache.BroadcastMsgCache;
import com.bin.im.server.repositories.sql.sys.SystemMsgContent;
import com.bin.im.server.spi.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  对内存中的广播消息进行管理
 */
@Service
public class BroadcastMsgMemoryHandler extends BaseHandler {


    public static final String SERVICE_NAME = "BroadcastMsgMemoryHandler";


    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }



    public Map<Long,String> findBroadcastMsgInMemory(List<Long> msgIds) {


        Map<Long,String> sysMsgContentMap = new HashMap<>();
        BiTuple<Map<Long, String>, List<Long>> sysBroadcastMsg = BroadcastMsgCache.getSysBroadcastMsg(msgIds);
        Map<Long, String> dataMap = sysBroadcastMsg.element1();

        if (dataMap != null && dataMap.size() > 0)
            sysMsgContentMap.putAll(dataMap);


        List<Long> emptyMsgIds = sysBroadcastMsg.element2();

        if (CollectionUtil.isNotEmpty(emptyMsgIds)) {
            List<SystemMsgContent> systemMsgContents = imDas().querySysMsgContents(emptyMsgIds).getMsg();
            for (SystemMsgContent systemMsgContent : systemMsgContents) {
                long msgId = systemMsgContent.getMsgId();
                String msgContent = systemMsgContent.getMsgContent();
                sysMsgContentMap.put(msgId,msgContent);
                BroadcastMsgCache.addNewSysMsg(msgId,msgContent);
            }
        }

        return sysMsgContentMap;
    }

}
