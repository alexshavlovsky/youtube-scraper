package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.executor.CustomExecutorService;
import com.ctzn.youtubescraper.model.channelvideos.ChannelDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.ChannelEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.persistence.repository.ChannelRepository;
import com.ctzn.youtubescraper.persistence.repository.VideoRepository;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PersistenceChannelRunner implements Callable<Void> {

    private final String channelId;
    private final PersistenceContext persistenceContext;
    private final int nThreads;
    private final long timeout;
    private final TimeUnit timeUnit;
    private final boolean sortNewestCommentsFirst;
    private final int videoCountLimit;
    private final int commentCountPerVideoLimit;
    private final int replyThreadCountLimit;

    public PersistenceChannelRunner(String channelId, PersistenceContext persistenceContext, int nThreads, long timeout, TimeUnit timeUnit) {
        this(channelId, persistenceContext, nThreads, timeout, timeUnit, true, 0, 0, 0);
    }

    public PersistenceChannelRunner(String channelId, PersistenceContext persistenceContext, int nThreads, long timeout, TimeUnit timeUnit, boolean sortNewestCommentsFirst, int commentCountPerVideoLimit, int replyThreadCountLimit) {
        this(channelId, persistenceContext, nThreads, timeout, timeUnit, sortNewestCommentsFirst, commentCountPerVideoLimit, replyThreadCountLimit, 0);
    }

    public PersistenceChannelRunner(String channelId, PersistenceContext persistenceContext, int nThreads, long timeout, TimeUnit timeUnit, boolean sortNewestCommentsFirst, int commentCountPerVideoLimit, int replyThreadCountLimit, int videoCountLimit) {
        this.channelId = channelId;
        this.persistenceContext = persistenceContext;
        this.nThreads = nThreads;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.sortNewestCommentsFirst = sortNewestCommentsFirst;
        this.commentCountPerVideoLimit = commentCountPerVideoLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
        this.videoCountLimit = videoCountLimit;
    }

    private Map<String, VideoEntity> grabChannelData(String channelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelDTO channel = collector.call();
        ChannelEntity channelEntity = ChannelEntity.fromChannelDTO(channel);
        List<VideoEntity> videoEntities =
                (videoCountLimit > 0 ?
                        channel.getVideos().stream().limit(videoCountLimit) :
                        channel.getVideos().stream()
                ).map(v -> VideoEntity.fromVideoDTO(v, channelEntity)).collect(Collectors.toList());
        persistenceContext.commitTransaction(session -> {
            ChannelRepository.saveOrUpdate(channelEntity, session);
            videoEntities.forEach(video -> VideoRepository.saveOrUpdate(video, session));
        });
        return videoEntities.stream().collect(LinkedHashMap::new, (map, video) -> map.put(video.getVideoId(), video), Map::putAll);
    }

    private void grabComments(Map<String, VideoEntity> videoEntityMap) throws InterruptedException {
        CustomExecutorService executor = new CustomExecutorService("CommentWorker" + channelId, nThreads, timeout, timeUnit);
        videoEntityMap.keySet().stream()
                .map(videoId -> new PersistenceCommentRunner(videoId, videoEntityMap, persistenceContext, sortNewestCommentsFirst, commentCountPerVideoLimit, replyThreadCountLimit))
                .forEach(executor::submit);
        executor.awaitAndTerminate();
    }

    @Override
    public Void call() throws Exception {
        grabComments(grabChannelData(channelId));
        return null;
    }
}
