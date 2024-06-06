package com.bin.im.common.id.snowflake;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SystemClock {

    /**
     * 线程名--系统时钟
     */
    public static final String THREAD_CLOCK_NAME="System Clock";
    
    private final long period;
    
    private final AtomicLong now;
    
    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }
    
    private static class InstanceHolder {
        public static final SystemClock INSTANCE = new SystemClock(1);
    }
    
    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }
    
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduledpool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setName(THREAD_CLOCK_NAME);
                thread.setDaemon(true);
                return thread;
            }
        });
        scheduledpool.scheduleAtFixedRate(() -> {
            now.set(System.currentTimeMillis());
        }, period, period, TimeUnit.MILLISECONDS);
    }
    
    private long currentTimeMillis() {
        return now.get();
    }
    
    public static long now() {
        return instance().currentTimeMillis();
    }
    
    public static String nowDate() {
        return new Timestamp(instance().currentTimeMillis()).toString();
    }
}
