package com.bin.im.server.repositories;

import com.bin.im.common.id.self.IdGenerate;
import com.bin.im.server.spi.impl.ServerContext;
import com.bin.im.server.spi.impl.ServiceManager;

public class MsgGenerator {

    /**
     *  保证同一个发送者在 服务器是绝对有序的
     *  eg: 这里处理出现乱序的的可能是 server服务端重启 然后新请求负载到其他 server服务端
     *  由于每个server服务端  long datacenterId 数据中心  long machineId 机器
     *  都不一样，所以如果负载到小的数据/机器中心 在同一毫秒 msgid 生成 无法 递增
     *  TODO 应该没有两次 sendMsg 间隔是 1 毫秒 , 做兜底处理以下
     */
    public static long generateMsgId() {
        ServerContext currContext = ServiceManager.getCurrContext();
        if (currContext == null) {
           return IdGenerate.generateMsgId();
        }
        return ServiceManager.getCurrContext().generateMsgId();
    }

    public static long generateSysMsgId() {
        return IdGenerate.generateMsgId();
    }
}
