package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.executor.CustomExecutorService;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

import java.util.concurrent.TimeUnit;

import static com.ctzn.youtubescraper.runner.CommentRunnerFactory.newDefaultFileAppender;


public class ChannelAllCommentsExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException, InterruptedException {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelDTO channel = collector.call();

        CustomExecutorService executor = new CustomExecutorService("CommentWorker",
                10, 10, TimeUnit.MINUTES);
        channel.videos.stream().map(
                v -> newDefaultFileAppender(v.getVideoId(), true)
        ).forEach(executor::submit);
        executor.awaitAndTerminate();
    }
}
