package com.ctzn.youtubescraper.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomExecutorService {

    private final CustomThreadFactory threadFactory;
    private final ExecutorService executor;
    private final long timeout;
    private final TimeUnit timeUnit;

    public CustomExecutorService(String threadNamePrefix, int nThreads, long timeout, TimeUnit timeUnit) {
        threadFactory = new CustomThreadFactory(threadNamePrefix);
        executor = Executors.newFixedThreadPool(nThreads, threadFactory);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void submit(Runnable runnable) {
        executor.submit(runnable);
    }

    public void awaitAndTerminate() throws InterruptedException {
        threadFactory.awaitAndTerminate(executor, timeout, timeUnit);
    }
}
