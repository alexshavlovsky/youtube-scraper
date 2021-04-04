package com.ctzn.youtubescraper.core.executor;

import com.ctzn.youtubescraper.core.config.ExecutorCfg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomExecutorService {

    private final CustomThreadFactory threadFactory;
    private final ExecutorService executor;
    private final long timeout;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    static public CustomExecutorService newInstance() {
        return new CustomExecutorService(ExecutorCfg.newInstance());
    }

    static public CustomExecutorService newInstance(ExecutorCfg cfg) {
        return new CustomExecutorService(cfg);
    }

    static public ExecutorCfg.ExecutorCfgBuilder configure(){
        return ExecutorCfg.builder();
    }

    private CustomExecutorService(ExecutorCfg cfg) {
        threadFactory = new CustomThreadFactory(cfg.getThreadNamePrefix());
        executor = Executors.newFixedThreadPool(cfg.getNumberOfThreads(), threadFactory);
        this.timeout = TIME_UNIT.convert(cfg.getTimeout());
    }

    public void submit(Runnable runnable) {
        executor.submit(runnable);
    }

    public void awaitAndTerminate() throws InterruptedException {
        threadFactory.awaitAndTerminate(executor, timeout, TIME_UNIT);
    }

}
