package com.ctzn.youtubescraper.config;

import com.ctzn.youtubescraper.executor.CustomExecutorService;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder(buildMethodName = "toBuilder")
public class ExecutorCfg {

    @Builder.Default
    public String threadNamePrefix = "YtsWorker";

    @Builder.Default
    public int numberOfThreads = 10;

    @Builder.Default
    public Duration timeout = Duration.ofDays(1);

    public void addThreadNameSegment(String nameSegment) {
        threadNamePrefix = threadNamePrefix + "-" + nameSegment;
    }

    public static ExecutorCfg newInstance() {
        return ExecutorCfg.builder().toBuilder();
    }

    public CustomExecutorService build() {
        return CustomExecutorService.newInstance(this);
    }

    @Override
    public String toString() {
        return "numberOfThreads=" + numberOfThreads + ", timeout=" + timeout;
    }

}
