package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

public class ChannelVideosExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) throws ScraperException {
        String chanelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(chanelId);
        ChannelDTO channel = collector.call();
        System.out.println(channel);
    }
}
