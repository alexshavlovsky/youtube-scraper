package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.ChannelEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PersistenceChannelRunner implements Callable<Void> {

    private final String channelId;
    private final PersistenceContext persistenceContext;
    private final int nThreads;
    private final long timeout;
    private final TimeUnit timeUnit;

    public PersistenceChannelRunner(String channelId, PersistenceContext persistenceContext, int nThreads, long timeout, TimeUnit timeUnit) {
        this.channelId = channelId;
        this.persistenceContext = persistenceContext;
        this.nThreads = nThreads;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    private Map<String, VideoEntity> grabChannelData(String chanelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(chanelId);
        ChannelDTO channel = collector.call();
        ChannelEntity channelEntity = ChannelEntity.fromChannelDTO(channel);
        List<VideoEntity> videoEntities = channel.getVideos().stream().map(v -> VideoEntity.fromVideoDTO(v, channelEntity)).collect(Collectors.toList());
        persistenceContext.commitTransaction(session -> {
            session.saveOrUpdate(channelEntity);
            videoEntities.forEach(session::saveOrUpdate);
        });
        return videoEntities.stream().collect(Collectors.toMap(VideoEntity::getVideoId, e -> e));
    }

    private void grabComments(Map<String, VideoEntity> videoEntityMap) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        videoEntityMap.keySet().stream().map(videoId -> new PersistenceCommentRunner(videoId, videoEntityMap, persistenceContext)).forEach(executor::submit);
        executor.shutdown();
        if (!executor.awaitTermination(timeout, timeUnit)) executor.shutdownNow();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Override
    public Void call() throws Exception {
        grabComments(grabChannelData(channelId));
        return null;
    }
}
