package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.config.CommentOrderCfg;
import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.executor.CustomExecutorService;
import com.ctzn.youtubescraper.model.channelvideos.ChannelDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

import java.time.Duration;

import static com.ctzn.youtubescraper.runner.CommentRunnerFactory.newDefaultFileAppender;


public class ChannelAllCommentsExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException, InterruptedException {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelDTO channel = collector.call();

        CustomExecutorService executor = CustomExecutorService.configure()
                .numberOfThreads(10).timeout(Duration.ofMinutes(10)).toBuilder().build();

        channel.videos.stream().map(
                v -> newDefaultFileAppender(v.getVideoId(), CommentOrderCfg.NEWEST_FIRST)
        ).forEach(executor::submit);

        executor.awaitAndTerminate();
    }

}
