package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChannelAllCommentsExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException {
        String chanelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(chanelId);
        ChannelDTO channel = collector.call();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        channel.videos.stream().map(VideoDTO::getVideoId)
                .map(CommentRunnerFactory::newNewestCommentsFirstFileAppenderRunner).forEach(executor::submit);
        executor.shutdown();
    }
}
