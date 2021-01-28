package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;
import com.ctzn.youtubescraper.executor.CustomExecutorService;

import java.util.concurrent.TimeUnit;


public class ChannelAllCommentsExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException, InterruptedException {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelDTO channel = collector.call();

        CustomExecutorService executor = new CustomExecutorService(
                "CommentWorker" + channelId, 10, 10, TimeUnit.MINUTES);
        channel.videos.stream().map(VideoDTO::getVideoId)
                .map(CommentRunnerFactory::newNewestCommentsFirstFileAppenderRunner).forEach(executor::submit);
        executor.awaitAndTerminate();
    }
}
