package com.bin.im.common.id.self;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;


public class IdGenerate {


    private static final AtomicLong sessionID = new AtomicLong();
    private static long ID_FLAG = 1260000000000L;

    public static long generateSessionId() {
        return generateSeqId();
    }

    public static long generateSeqId() {
        long infoid = System.nanoTime() - ID_FLAG;
        infoid = (infoid << 7) | sessionID.getAndIncrement();
        return infoid;
    }

    public static long getLogId() {
       return genLogId(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt());
    }

    public static long genLogId(long param) {
        long nowTime = System.currentTimeMillis();
        return nowTime & 0x7FFFFFFF | (param >> 8 & 65535L) << 47;
    }


    private static final SnowflakeIdUtils msgId = new SnowflakeIdUtils(1, 1);

    public static long generateMsgId() {
        return msgId.nextId();
    }

    public static long generateMsgId(long datacenterId,long machineId) {
        return msgId.nextId(datacenterId,machineId);
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }


}
