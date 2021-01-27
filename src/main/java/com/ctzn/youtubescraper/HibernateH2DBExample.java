package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.db.PersistenceContext;
import com.ctzn.youtubescraper.entity.ChannelEntity;
import com.ctzn.youtubescraper.entity.VideoEntity;
import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;
import com.ctzn.youtubescraper.runner.PersistenceRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HibernateH2DBExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    private static Map<String, VideoEntity> grabChannelData(String chanelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(chanelId);
        ChannelDTO channel = collector.call();
        ChannelEntity channelEntity = ChannelEntity.fromChannelDTO(channel);
        List<VideoEntity> videoEntities = channel.getVideos().stream().map(v -> VideoEntity.fromVideoDTO(v, channelEntity)).collect(Collectors.toList());
        PersistenceContext.commitTransaction(session -> {
            session.saveOrUpdate(channelEntity);
            videoEntities.forEach(session::saveOrUpdate);
        });
        return videoEntities.stream().collect(Collectors.toMap(VideoEntity::getVideoId, e -> e));
    }

    private static void grabComments(Map<String, VideoEntity> videoEntityMap) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        videoEntityMap.keySet().stream().map(videoId -> new PersistenceRunner(videoId, videoEntityMap)).forEach(executor::submit);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    public static void main(String[] args) throws ScraperException, InterruptedException {
        String chanelId = "UCXe92kCAWPdWRW7Ylfku_rw";
        Map<String, VideoEntity> videoEntityMap = grabChannelData(chanelId);
        grabComments(videoEntityMap);
    }
}
