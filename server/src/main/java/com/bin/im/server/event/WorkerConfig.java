package com.bin.im.server.event;

import java.util.concurrent.ExecutorService;

import static com.bin.im.server.event.StripedExecutor.WORKER_DEFAULT;

public class WorkerConfig {


    private String business = WORKER_DEFAULT;

    private ExecutorService executorService;


    public static WorkerConfig  defaultWorker() {

        return null;
    }

    public WorkerConfig(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public WorkerConfig(String business, ExecutorService executorService) {
        this.business = business;
        this.executorService = executorService;
    }

    public String getBusiness() {
        return business;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
