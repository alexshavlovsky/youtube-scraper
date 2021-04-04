package com.ctzn.youtubescraper.examples.filesystem;

import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.executor.CustomExecutorService;
import com.ctzn.youtubescraper.core.persistence.ChannelVideosCollector;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelVideosDTO;

import java.time.Duration;

import static com.ctzn.youtubescraper.addons.customhandlers.CustomCommentRunnerFactory.newDefaultFileAppender;


public class ChannelAllCommentsExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException, InterruptedException {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";

        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelVideosDTO channelVideos = collector.call();

        CustomExecutorService executor = CustomExecutorService.configure()
                .numberOfThreads(10).timeout(Duration.ofMinutes(10)).toBuilder().build();

        channelVideos.getVideos().stream().map(
                v -> newDefaultFileAppender(v.getVideoId(), CommentOrderCfg.NEWEST_FIRST)
        ).forEach(executor::submit);

        executor.awaitAndTerminate();
    }

}
