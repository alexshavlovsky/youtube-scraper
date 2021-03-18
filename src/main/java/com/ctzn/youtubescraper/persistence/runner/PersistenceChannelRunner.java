package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.executor.CustomExecutorService;
import com.ctzn.youtubescraper.model.channelvideos.ChannelDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.ChannelEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.persistence.entity.WorkerLogEntity;
import com.ctzn.youtubescraper.persistence.repository.ChannelRepository;
import com.ctzn.youtubescraper.persistence.repository.VideoRepository;
import com.ctzn.youtubescraper.persistence.repository.WorkerLogRepository;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PersistenceChannelRunner implements Callable<Void> {

    final static int PROCESS_ALL_VIDEOS = 0;

    private final String channelId;
    private final PersistenceContext persistenceContext;
    private final int nThreads;
    private final long timeout;
    private final TimeUnit timeUnit;
    private final boolean sortNewestCommentsFirst;
    private final int videoCountLimit;
    private final int commentCountPerVideoLimit;
    private final int replyThreadCountLimit;

    PersistenceChannelRunner(String channelId, PersistenceContext persistenceContext, int nThreads, long timeout, TimeUnit timeUnit, boolean sortNewestCommentsFirst, int commentCountPerVideoLimit, int replyThreadCountLimit, int videoCountLimit) {
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

    public static PersistenceChannelRunnerConfigurer.PersistenceChannelRunnerConfigurerBuilder newBuilder(String channelId) {
        return PersistenceChannelRunnerConfigurer.newInstance(channelId);
    }

    private Map<String, VideoEntity> grabChannelData(String channelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelDTO channel = collector.call();
        ChannelEntity channelEntity = ChannelEntity.fromChannelDTO(channel);
        List<VideoEntity> videoEntities =
                (videoCountLimit == PROCESS_ALL_VIDEOS ?
                        channel.getVideos().stream() :
                        channel.getVideos().stream().limit(videoCountLimit)
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
        WorkerLogEntity logEntry = new WorkerLogEntity(null, channelId, new Date(), null, "STARTED", toString());
        persistenceContext.commitTransaction(session -> WorkerLogRepository.save(logEntry, session));
        try {
            Map<String, VideoEntity> videoEntityMap = grabChannelData(channelId);
            grabComments(videoEntityMap);
            logEntry.setFinishedDate(new Date());
            logEntry.setStatus("DONE: videoCount=" + videoEntityMap.size());
            persistenceContext.commitTransaction(session -> WorkerLogRepository.saveOrUpdate(logEntry, session));
        } catch (Exception e) {
            logEntry.setFinishedDate(new Date());
            logEntry.setStatus("EXCEPTION: " + e.getMessage());
            persistenceContext.commitTransaction(session -> WorkerLogRepository.saveOrUpdate(logEntry, session));
            throw e;
        }
        return null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PersistenceChannelRunner.class.getSimpleName() + "[", "]")
                .add("nThreads=" + nThreads)
                .add("timeout=" + timeout)
                .add("timeUnit=" + timeUnit)
                .add("sortNewestCommentsFirst=" + sortNewestCommentsFirst)
                .add("videoCountLimit=" + videoCountLimit)
                .add("commentCountPerVideoLimit=" + commentCountPerVideoLimit)
                .add("replyThreadCountLimit=" + replyThreadCountLimit)
                .toString();
    }

}
