package com.ctzn.youtubescraper.examples;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.persistence.ChannelVideosCollector;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelVideosDTO;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;

import java.util.List;

public class ChannelVideosExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) throws ScraperException {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelVideosDTO channel = collector.call();
        List<VideoDTO> videos = channel.getVideos();
        for (int i = 0; i < videos.size(); i++) {
            VideoDTO video = videos.get(i);
            System.out.println(String.format("%s [%s] %s", i + 1, video.getVideoId(), video.getTitle()));
        }
    }
}
