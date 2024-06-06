package com.bin.im.common.mini.buffer;


import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.numberOfLeadingZeros;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;

public final class NioBufferPool {

    private static final int NUMBER_OF_SLABS = 33;


    private static final int MIN_SIZE = 0;


    private static final int MAX_SIZE = 0;

    private static final boolean MIN_MAX_CHECKS = MIN_SIZE != 0 || MAX_SIZE != 0;


    /**
     *  调用堆栈分析
     */
    static final boolean REGISTRY = false;


    /**
     *  pool 使用状态收集
     */
    static final boolean STATS = false;



    static final boolean USE_WATCHDOG = false;
    static final Duration WATCHDOG_INTERVAL = Duration.ofSeconds(2);
    static final Duration WATCHDOG_SMOOTHING_WINDOW = Duration.ofSeconds(10);
    static final double WATCHDOG_ERROR_MARGIN = 4.0;
    private static final double SMOOTHING_COEFF = 1.0 - Math.pow(0.5, (double) WATCHDOG_INTERVAL.toMillis() / WATCHDOG_SMOOTHING_WINDOW.toMillis());


    static final BufferConcurrentQueue[] slabs;
    static final SlabStats[] slabStats;
    static final AtomicInteger[] created;
    static final AtomicInteger[] reused;

    private static final BufferPoolStats stats = new BufferPoolStats();


    public static final class Entry {
        final int size;
        final long timestamp;
        final Thread thread;
        final StackTraceElement[] stackTrace;

        Entry(int size, long timestamp, Thread thread, StackTraceElement[] stackTrace) {
            this.size = size;
            this.timestamp = timestamp;
            this.thread = thread;
            this.stackTrace = stackTrace;
        }

        public int getSize() {
            return size;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getAge() {
            return Duration.ofMillis(System.currentTimeMillis() - timestamp).toString();
        }

        public String getThread() {
            return thread.toString();
        }

        public List<String> getStackTrace() {
            return Arrays.stream(stackTrace).map(StackTraceElement::toString).collect(toList());
        }

        @Override
        public String toString() {
            return "{" +
                    "size=" + size +
                    ", timestamp=" + timestamp +
                    ", thread=" + thread +
                    ", stackTrace=" + Arrays.toString(stackTrace) +
                    '}';
        }
    }

    private static final Map<NioBuffer, Entry> allocateRegistry = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<NioBuffer, Entry> recycleRegistry = Collections.synchronizedMap(new WeakHashMap<>());

    static {
        slabs = new BufferConcurrentQueue[NUMBER_OF_SLABS];
        slabStats = new SlabStats[NUMBER_OF_SLABS];
        created = new AtomicInteger[NUMBER_OF_SLABS];
        reused = new AtomicInteger[NUMBER_OF_SLABS];
        for (int i = 0; i < NUMBER_OF_SLABS; i++) {
            slabs[i] = new BufferConcurrentQueue();
            created[i] = new AtomicInteger();
            reused[i] = new AtomicInteger();
        }
        if (USE_WATCHDOG) {
            for (int i = 0; i < NUMBER_OF_SLABS; i++) {
                slabStats[i] = new SlabStats();
            }
            Thread watchdogThread = new Thread(() -> {
                while (true) {
                    updateStats();
                    evict();
                    try {
                        //noinspection BusyWait
                        Thread.sleep(WATCHDOG_INTERVAL.toMillis());
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "bytebufpool-watchdog-thread");
            watchdogThread.setDaemon(true);
            watchdogThread.setPriority(Thread.MIN_PRIORITY);
            watchdogThread.start();
        }
    }


    private NioBufferPool(){}


    public static NioBuffer allocate(int size) {
        if (MIN_MAX_CHECKS) {
            if ((MIN_SIZE != 0 && size < MIN_SIZE) || (MAX_SIZE != 0 && size >= MAX_SIZE)) {
                // not willing to register in pool
                throw new RuntimeException("size unqualified");
            }
        }
        int index = 32 - numberOfLeadingZeros(size - 1); // index==32 for size==0
        BufferConcurrentQueue queue = slabs[index];
        NioBuffer buf = queue.poll();
        if (buf != null) {
            buf.resetOffsets();
            buf.refs = 1;
            if (STATS) recordReuse(index);
        }else {
            buf = new NioBuffer(ByteBuffer.allocate(index == 32 ? 0 : 1 << index));
            buf.refs = 1;
            if (STATS) recordNew(index);
        }
        if (REGISTRY) allocateRegistry.put(buf, buildRegistryEntry(buf));
        return buf;
    }




    static void recycle(NioBuffer buffer) {
        int slab = 32 - numberOfLeadingZeros(buffer.capacity() - 1);
        BufferConcurrentQueue queue = slabs[slab];
        if (REGISTRY) {
            recycleRegistry.put(buffer, buildRegistryEntry(buffer));
            allocateRegistry.remove(buffer);
        }
        queue.offer(buffer);
    }



    public static void clear() {
        for (int i = 0;i < NUMBER_OF_SLABS;i++) {
            slabs[i].clear();
            created[i].set(0);
            reused[i].set(0);
            if (USE_WATCHDOG) slabStats[i].clear();
        }
        allocateRegistry.clear();
        recycleRegistry.clear();
    }


    private static void recordNew(int index) {
        created[index].incrementAndGet();
    }

    private static void recordReuse(int index) {
        reused[index].incrementAndGet();
    }

    private static Entry buildRegistryEntry(NioBuffer buf) {
        Thread thread = currentThread();
        StackTraceElement[] stackTrace = thread.getStackTrace();
        return new Entry(buf.capacity(), currentTimeMillis(), thread,
                Arrays.copyOfRange(stackTrace, 4, stackTrace.length));
    }


    static AssertionError onByteBufRecycled(NioBuffer buf) {
        int slab = 32 - numberOfLeadingZeros(buf.capacity() - 1);
        BufferConcurrentQueue queue = slabs[slab];
        queue.clear();
        return new AssertionError("Attempt to use recycled ByteBuf" +
                (REGISTRY ? getByteBufTrace(buf) : ""));
    }

    static String getByteBufTrace(NioBuffer buf) {
        Entry allocated = allocateRegistry.get(buf);
        Entry recycled = recycleRegistry.get(buf);
        if (allocated == null && recycled == null) return "";
        return "\nAllocated: " + allocated +
                "\nRecycled: " + recycled;
    }

    public static BufferPoolStatsMXBean getStats() {
        return stats;
    }

    public interface BufferPoolStatsMXBean {
        int getCreatedItems();

        int getReusedItems();

        int getPoolItems();

        long getPoolSize();

        long getPoolSizeKB();

        long getTotalSlabMins();

        long getTotalEvicted();

        List<String> getPoolSlabs();

        List<Entry> queryUnrecycledBufs(int limit);

        void clear();

        void clearRegistry();
    }

    public static final class BufferPoolStats implements BufferPoolStatsMXBean {
        @Override
        public int getCreatedItems() {
            return stream(created).mapToInt(AtomicInteger::get).sum();
        }

        @Override
        public int getReusedItems() {
            return stream(reused).mapToInt(AtomicInteger::get).sum();
        }

        @Override
        public int getPoolItems() {
            return stream(slabs).mapToInt(BufferConcurrentQueue::size).sum();
        }

        @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
        public String getPoolItemsString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <  NUMBER_OF_SLABS; ++i) {
                int createdItems = created[i].get();
                int poolItems = slabs[i].size();
                if (createdItems != poolItems) {
                    sb.append(String.format("Slab %d (%d) ", i, (1 << i)))
                            .append(" created: " + createdItems)
                            .append(" pool: " + poolItems + "\n");
                }
            }
            return sb.toString();
        }

        @Override
        public long getPoolSize() {
            long result = 0;
            for (int i = 0; i < slabs.length - 1; i++) {
                long slabSize = 1L << i;
                result += slabSize * slabs[i].size();
            }
            return result;
        }

        @Override
        public long getPoolSizeKB() {
            return getPoolSize() / 1024;
        }

        @Override
        public long getTotalSlabMins() {
            if (!USE_WATCHDOG) return -1;
            long totalSlabMins = 0;
            for (BufferConcurrentQueue slab : slabs) {
                totalSlabMins += slab.realMin.get();
            }
            return totalSlabMins;
        }

        @Override
        public long getTotalEvicted() {
            if (!USE_WATCHDOG) return -1;
            long totalEvicted = 0;
            for (SlabStats slabStat : slabStats) {
                totalEvicted += slabStat.evictedTotal;
            }
            return totalEvicted;
        }

        public Map<NioBuffer, Entry> getUnrecycledBufs() {
            return new HashMap<>(allocateRegistry);
        }

        @Override
        public List<Entry> queryUnrecycledBufs(int limit) {
            if (limit < 1) throw new IllegalArgumentException("Limit must be >= 1");
            Map<NioBuffer, Entry> danglingBufs = getUnrecycledBufs();
            return danglingBufs.values().stream().sorted(comparingLong(Entry::getTimestamp)).limit(limit).collect(toList());
        }

        @Override
        public List<String> getPoolSlabs() {
            List<String> result = new ArrayList<>(slabs.length + 1);
            String header = "SlotSize,Created,Reused,InPool,Total(Kb)";
            if (USE_WATCHDOG) header += ",RealMin,EstMean,Error,Evicted";
            result.add(header);
            for (int i = 0; i < slabs.length; i++) {
                int idx = (i + 32) % slabs.length;
                long slabSize = idx == 32 ? 0 : 1L << idx;
                BufferConcurrentQueue slab = slabs[idx];
                int count = slab.size();
                String slabInfo = slabSize + "," +
                        (STATS ? created[idx] : "-") + "," +
                        (STATS ? reused[idx] : "-") + "," +
                        count + "," +
                        slabSize * count / 1024;
                if (USE_WATCHDOG){
                    SlabStats slabStat = slabStats[idx];
                    slabInfo += "," + slab.realMin.get() + "," +
                            String.format("%.1f", slabStat.estimatedMin) + "," +
                            String.format("%.1f", slabStat.estimatedError) + "," +
                            slabStat.evictedTotal;
                }

                result.add(slabInfo);
            }
            return result;
        }

        @Override
        public void clear() {
            NioBufferPool.clear();
        }

        @Override
        public void clearRegistry() {
            allocateRegistry.clear();
            recycleRegistry.clear();
        }
    }

    // region watchdog
    private static final class SlabStats {
        double estimatedMin;  // 估计
        int evictedTotal;
        int evictedLast;
        int evictedMax;
        double estimatedError;

        void clear() {
            estimatedMin = estimatedError = evictedTotal = evictedLast = evictedMax = 0;
        }

        @Override
        public String toString() {
            return "SlabStats{" +
                    "estimatedMin=" + estimatedMin +
                    ", estimatedError=" + estimatedError +
                    ", evictedTotal=" + evictedTotal +
                    ", evictedLast=" + evictedLast +
                    ", evictedMax=" + evictedMax +
                    '}';
        }
    }

    private static void updateStats() {
        for (int i = 0; i < slabs.length; i++) {
            SlabStats stats = slabStats[i];
            BufferConcurrentQueue slab = slabs[i];
            int realMin = slab.realMin.getAndSet(slab.size());

            double realError = Math.abs(stats.estimatedMin - realMin);
            stats.estimatedError += (realError - stats.estimatedError) * SMOOTHING_COEFF;

            if (realMin < stats.estimatedMin) {
                stats.estimatedMin = realMin;
            } else {
                stats.estimatedMin += (realMin - stats.estimatedMin) * SMOOTHING_COEFF;
            }
        }
    }

    private static void evict() {
        for (int i = 0; i < slabs.length; i++) {
            BufferConcurrentQueue slab = slabs[i];
            SlabStats stats = slabStats[i];
            int evictCount = (int) Math.round(stats.estimatedMin - stats.estimatedError * WATCHDOG_ERROR_MARGIN);
            stats.evictedLast = 0;
            for (int j = 0; j < evictCount; j++) {
                NioBuffer buf = slab.poll();
                if (buf == null) break;
                stats.estimatedMin--;
                stats.evictedLast++;
                if (REGISTRY) recycleRegistry.remove(buf);
            }
            stats.evictedTotal += stats.evictedLast;
            stats.evictedMax = Math.max(stats.evictedLast, stats.evictedMax);
        }
    }
    //endregion
}
