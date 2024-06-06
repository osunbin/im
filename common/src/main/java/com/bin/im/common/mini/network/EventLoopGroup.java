package com.bin.im.common.mini.network;

import com.bin.im.common.mini.buffer.NioBuffer;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLoopGroup {


    private final EventLoop[] writeWorkers;

    private  EventLoop[] acceptWorkers;

    private final ExecutorService writeExecutorService;

    private final AtomicInteger writeIndex = new AtomicInteger(0);

    private IoListener messageListener;



    public EventLoopGroup() throws IOException {
        this(null,false);
    }

    public EventLoopGroup(IoListener messageListener) throws IOException {
        this(messageListener,true);
    }

    public EventLoopGroup(IoListener messageListener, boolean server) throws IOException {
        if (messageListener == null && server) {
            throw new NullPointerException("messageListener is null");
        }

        int writeThreadNum = 1;
        writeExecutorService = getThreadPoolExecutor("io-write-", writeThreadNum);

        this.writeWorkers = new EventLoop[writeThreadNum];

        if (server)
             acceptWorkers = writeWorkers;

        for (int i = 0; i < writeThreadNum; i++) {
            writeWorkers[i] = new EventLoop(Selector.open());
            writeExecutorService.execute(writeWorkers[i]);
        }
        this.messageListener = messageListener;
    }


    public EventLoop getAcceptWorker() {
        return acceptWorkers[index(acceptWorkers.length, writeIndex)];
    }


    public EventLoop getWriteWorker() {
        return writeWorkers[index(writeWorkers.length, writeIndex)];
    }

    public void onMessage(NioBuffer nioBuffer) {
        if (messageListener != null)
               messageListener.onMessage(nioBuffer);
    }

    public IoListener getMessageListener() {
        return messageListener;
    }

    public void shutdownNow() {
        writeExecutorService.shutdownNow();
        for (EventLoop write : writeWorkers) {
            write.close();
        }
    }

    private int index(int arrayLength, AtomicInteger index) {
        int i = index.getAndIncrement() % arrayLength;
        if (i < 0) {
            i = -i;
        }
        return i;
    }

    private ThreadPoolExecutor getThreadPoolExecutor(final String prefix, int threadNum) {
        return new ThreadPoolExecutor(threadNum, threadNum, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, prefix + atomicInteger.getAndIncrement());
            }
        });
    }

}
