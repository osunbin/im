package com.bin.im.common.id.self;

import com.bin.im.common.id.baidu.DefaultUidGenerator;
import com.bin.im.common.id.snowflake.SnowflakeIdWorker;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 雪花算法工具类
 */
public class SnowflakeIdUtils {
    // 开始时间戳（2020-01-01）
    private static final long START_TIMESTAMP = 1577836800000L;
 
    // 每部分所占位数
    private static final long SEQUENCE_BIT = 12; // 序列号占用位数
    private static final long MACHINE_BIT = 5;   // 机器标识占用位数
    private static final long DATACENTER_BIT = 5;// 数据中心占用位数
 
    // 每部分的最大值
    private static final long MAX_SEQUENCE_NUM = ~(-1L << SEQUENCE_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
 
    // 每部分向左的位移
    private static final long MACHINE_LEFT_SHIFT = SEQUENCE_BIT;
    private static final long DATACENTER_LEFT_SHIFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BIT + MACHINE_BIT + DATACENTER_BIT;
 
    private final long datacenterId; // 数据中心标识
    private final long machineId;    // 机器标识
    private long sequence = 0L;      // 序列号
    private long lastTimestamp = -1L;// 上次生成ID的时间戳
    private int correct = 0;
    /**
     * 构造方法
     *
     * @param datacenterId 数据中心标识，取值范围 [0, 31]
     * @param machineId    机器标识，取值范围 [0, 31]
     */
    public SnowflakeIdUtils(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter Id cannot be greater than " + MAX_DATACENTER_NUM + " or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("Machine Id cannot be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }
 
    /**
     * 生成下一个ID
     *
     * @return long类型的ID
     */
    public synchronized long nextId(long currentTimestamp) {

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }
 
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE_NUM;
            if (sequence == 0) {
                // 当前毫秒的序列号已经用完，等待下一个毫秒
                currentTimestamp = waitNextMillis();
            }
        } else {
            // 随机生成[0,10)
            sequence = ThreadLocalRandom.current().nextInt(10);
        }
 
        lastTimestamp = currentTimestamp;
 
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_LEFT_SHIFT)
                | (machineId << MACHINE_LEFT_SHIFT)
                | sequence;
    }

    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimestamp();
        return nextId(currentTimestamp);
    }

    public synchronized long nextId( long datacenterId, long machineId) {
        long currentTimestamp = 0;
        if (this.datacenterId < datacenterId  || (this.datacenterId == datacenterId && this.machineId < machineId)) {
            currentTimestamp = getNextTimestamp();
            lastTimestamp = currentTimestamp;
        } else {
            currentTimestamp = getCurrentTimestamp();
        }
       return nextId(currentTimestamp);
    }
 
    /**
     * 生成下一个ID
     *
     * @return String类型的ID
     */
    public synchronized String nextIdString() {
        return String.valueOf(nextId());
    }
 
    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public long getCurrentTimestamp() {
        return System.currentTimeMillis() + correct;
    }

    public long getNextTimestamp() {
        ++correct;
        return System.currentTimeMillis() + 1;
    }


    /**
     * 若当前毫秒的序列号已经用完，则等待下一个毫秒
     *
     * @return 下一个毫秒的时间戳
     */
    private long waitNextMillis() {
        long currentTimestamp = getCurrentTimestamp();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }
 
    /**
     * 测试方法，生成100个ID并打印
     *
     *   579805472484364290    1 1
     *   579805472484642816    3 5
     *   579805472484519941    2 7
     *
     */
    public static void main(String[] args) {

        DefaultUidGenerator uidGenerator = new DefaultUidGenerator();
        uidGenerator.afterPropertiesSet();
        long uid = uidGenerator.getUID();
        System.out.println(uid);
        long datacenterId = 1; // 数据中心标识
        long machineId = 1;    // 机器标识

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(machineId,datacenterId);

        System.out.println(snowflakeIdWorker.nextId());
        SnowflakeIdUtils snowflake1 = new SnowflakeIdUtils(1, 1);


        System.out.println("1-1 " + snowflake1.nextId(1,2));


        System.out.println("1-1 " + snowflake1.nextId());

      //  TimeUtils.sleeps(2);
        SnowflakeIdUtils snowflake2 = new SnowflakeIdUtils(3, 5);
        System.out.println("3-5 " + snowflake2.nextId());

      //  TimeUtils.sleeps(2);

        SnowflakeIdUtils snowflake4 = new SnowflakeIdUtils(9, 1);
        System.out.println("9-1 " + snowflake4.nextId());
      //  TimeUtils.sleeps(2);
        SnowflakeIdUtils snowflake3 = new SnowflakeIdUtils(2, 7);
        System.out.println("2-7 " + snowflake3.nextId());


    }
}
/**
 *
 *   mysql
 *   defaultFetchSize 500
 *   useCursorFetch    true
 *    李锦猎人被查杀   
 */