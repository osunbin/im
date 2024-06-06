package com.bin.im.server.event;

import com.bin.im.server.ServerApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.bin.im.server.event.WorkerConfig.defaultWorker;
import static java.lang.Math.abs;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class StripedExecutor implements Executor {
    private static final Logger logger = LoggerFactory.getLogger(StripedExecutor.class);


    final static String WORKER_DEFAULT = "default";

    private final Map<String,Worker> workers ;
    private volatile boolean live = true;


    public StripedExecutor() {
        workers = new HashMap<>(1);

        Worker worker = new Worker(defaultWorker());
        workers.put(WORKER_DEFAULT,worker);
    }

    public StripedExecutor(List<WorkerConfig> workerConfigs) {

        this.workers =  new HashMap<>(workerConfigs.size());

        for (int i = 0; i < workerConfigs.size(); i++) {
            WorkerConfig workerConfig = workerConfigs.get(i);

            Worker worker = new Worker(workerConfig);
            workers.put(workerConfig.getBusiness(),worker);
        }
    }

    public void shutdown() {
        live = false;

        for (Worker worker : workers.values()) {
            worker.shutdown();
        }
    }

    public boolean isLive() {
        return live;
    }


    @Override
    public void execute(Runnable task) {

        if (!live) {
            throw new RejectedExecutionException("Executor is terminated!");
        }

        Worker worker = getWorker(task);
        worker.schedule(task);
    }

    private Worker getWorker(Runnable task) {

        if (task instanceof StripedRunnable) {
           String index = ((StripedRunnable) task).getKey();

           return workers.get(index);
        }

        return workers.get(WORKER_DEFAULT);
    }


    final class Worker  {
        private ExecutorService executorService;
        private String business;


        private Worker(WorkerConfig workerConfig) {
            this.executorService = workerConfig.getExecutorService();
            this.business = workerConfig.getBusiness();
        }

        private void schedule(Runnable task) {
            process(task);
        }


        private void process(Runnable task) {
            if (live) {
                try {
                    executorService.submit(task);
                } catch (Throwable e) {
                    logger.error("{} caught an exception while processing:{}", business, task, e);
                }
            }
        }

        private void shutdown() {
            executorService.shutdown();
        }

    }
}
