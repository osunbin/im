package com.bin.im.entry.http.handler;

import com.bin.im.entry.config.EntryConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpTaskPool {
    private static ExecutorService taskPool = taskPool =
            Executors.newFixedThreadPool(EntryConfig.ENTRY_CONFIG.getCpuCores()/2); ;


    public static void submitTask(Runnable task) {
        taskPool.submit(task);
    }

}
