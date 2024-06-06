package com.bin.im.common.id.baidu;

import com.bin.im.common.id.UidGenerator;
import com.bin.im.common.id.workerid.WorkerIdAssigner;
import com.bin.im.common.internal.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.bin.im.common.id.snowflake.SnowflakeIdWorker.getMachineNum;


/**
 * Represents an implementation of {@link UidGenerator}
 *
 * The unique id has 64bits (long), default allocated as blow:<br>
 * <li>sign: The highest bit is 0
 * <li>delta seconds: The next 28 bits, represents delta seconds since a customer epoch(2016-05-20 00:00:00.000).
 *                    Supports about 8.7 years until to 2024-11-20 21:24:16
 * <li>worker id: The next 22 bits, represents the worker's id which assigns based on database, max id is about 420W
 * <li>sequence: The next 13 bits, represents a sequence within the same second, max for 8192/s<br><br>
 *
 * The {@link DefaultUidGenerator#parseUID(long)} is a tool method to parse the bits
 *
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              22bits         13bits
 * }</pre>
 *
 * You can also specified the bits by Spring property setting.
 * <li>timeBits: default as 28
 * <li>workerBits: default as 22
 * <li>seqBits: default as 13
 * <li>epochStr: Epoch date string format 'yyyy-MM-dd'. Default as '2016-05-20'<p>
 *
 * <b>Note that:</b> The total bits must be 64 -1
 *
 *      a、可配置 delta seconds (28 bits)
 *            当前时间，相对于时间基点"2016-05-20"的增量值，单位：秒，最多可支持约8.7年
 *
 *      b、worker id (22 bits)
 *            机器id，最多可支持约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。
 *
 *     c、sequence (13 bits)
 *             秒下的并发序列，13 bits可支持每秒8192个并发。
 *
 *             注：三者之和为63
 *
 *     *对于并发数要求不高、期望长期使用的应用, 可增加```timeBits```位数, 减少```seqBits```位数.
 *                            例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天,
 *                            那么配置成```{"workerBits":23,"timeBits":31,"seqBits":9}```时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年.
 *
 *    *对于节点重启频率频繁、期望长期使用的应用, 可增加```workerBits```和```timeBits```位数, 减少```seqBits```位数.
 *                            例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天,
 *                            那么配置成```{"workerBits":27,"timeBits":30,"seqBits":6}```时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.
 *
 *
 */
public class DefaultUidGenerator implements UidGenerator{

    private static final Logger logger = LoggerFactory.getLogger(UidGenerator.class);

    /** Bits allocate */
    protected int timeBits = 28;
    protected int workerBits = 22;
    protected int seqBits = 13;

    /** Customer epoch, unit as second. For example 2016-05-20 (ms: 1463673600000)*/
    protected String epochStr = "2016-05-20";
    protected long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1463673600000L);

    /** Stable fields after spring bean initializing */
    protected BitsAllocator bitsAllocator;
    protected long workerId;

    /** Volatile fields caused by nextId() */
    protected long sequence = 0L;
    protected long lastSecond = -1L;



    public DefaultUidGenerator() {
        this(null,null);
    }


    public DefaultUidGenerator(WorkerIdAssigner workerIdAssigner) {
        this(workerIdAssigner,null);
    }

    public DefaultUidGenerator(WorkerIdAssigner workerIdAssigner,BitsAllocator bitsAllocator) {

        if (workerIdAssigner == null) {
            workerIdAssigner = () -> getMachineNum(31);
        }
        workerId = workerIdAssigner.assignWorkerId();
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new RuntimeException("Worker id " + workerId + " exceeds the max " + bitsAllocator.getMaxWorkerId());
        }

        this.bitsAllocator = bitsAllocator;
        if (bitsAllocator == null) {
            this.bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
        }

        logger.info("Initialized bits({}) for workerID:{}", bitsAllocator.toString(), workerId);

    }









    @Override
    public long getUID() {
        try {
            return nextId();
        } catch (Exception e) {
            logger.error("Generate unique id exception. ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String parseUID(long uid) {
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long deltaSeconds = uid >>> (workerIdBits + sequenceBits);


        String thatTimeStr = TimeUtils.formatSecond(epochSeconds + deltaSeconds);

        // format as string
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}",
                uid, thatTimeStr, workerId, sequence);
    }



    /**
     * Get UID
     *
     * @return UID
     * @throws Exception in the case: Clock moved backwards; Exceeds the max timestamp
     */
    protected synchronized long nextId() {
        long currentSecond = getCurrentSecond();

        // Clock moved backwards, refuse to generate uid
        if (currentSecond < lastSecond) {
            long refusedSeconds = lastSecond - currentSecond;
            throw new RuntimeException("Clock moved backwards. Refusing for "+refusedSeconds+" seconds");
        }

        // At the same second, increase sequence
        if (currentSecond == lastSecond) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            if (sequence == 0) {
                currentSecond = getNextSecond(lastSecond);
            }

            // At the different second, sequence restart from zero
        } else {
            sequence = 0L;
        }

        lastSecond = currentSecond;

        // Allocate bits for UID
        return bitsAllocator.allocate(currentSecond - epochSeconds, workerId, sequence);
    }

    /**
     * Get next millisecond
     */
    private long getNextSecond(long lastTimestamp) {
        long timestamp = getCurrentSecond();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentSecond();
        }

        return timestamp;
    }

    /**
     * Get current second
     */
    private long getCurrentSecond() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epochSeconds > bitsAllocator.getMaxDeltaSeconds()) {
            throw new RuntimeException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }

        return currentSecond;
    }


}
