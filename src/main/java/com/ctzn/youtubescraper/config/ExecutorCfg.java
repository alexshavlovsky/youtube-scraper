package com.ctzn.youtubescraper.config;

import com.ctzn.youtubescraper.executor.CustomExecutorService;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;

@Data
@Builder(buildMethodName = "toBuilder")
@ToString
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

}
