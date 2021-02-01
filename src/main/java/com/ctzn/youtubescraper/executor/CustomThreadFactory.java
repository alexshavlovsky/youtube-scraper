package com.ctzn.youtubescraper.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CustomThreadFactory implements ThreadFactory {

    private final List<Thread> threads = new ArrayList<>();
    private final String prefix;

    CustomThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, prefix + "-" + threads.size());
        threads.add(thread);
        return thread;
    }

    private List<Thread> getAliveThreads() {
        return threads.stream().filter(t -> t.getState() != Thread.State.TERMINATED).collect(Collectors.toList());
    }

    private void interruptAll(int timeout) throws InterruptedException {
        while (timeout-- > 0) {
            Thread.sleep(1000);
            List<Thread> th = getAliveThreads();
            if (th.size() == 0) break;
            th.forEach(Thread::interrupt);
        }
    }

    void awaitAndTerminate(ExecutorService executor, long timeout, TimeUnit timeUnit) throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(timeout, timeUnit)) {
            executor.shutdownNow();
            interruptAll(10);
        }
    }
}
