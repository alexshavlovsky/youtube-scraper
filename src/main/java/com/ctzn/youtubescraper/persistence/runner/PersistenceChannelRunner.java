package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.config.CommentOrderCfg;
import com.ctzn.youtubescraper.config.ExecutorCfg;
import com.ctzn.youtubescraper.config.VideoIteratorCfg;
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
import com.ctzn.youtubescraper.persistence.runner.stepbuilder.*;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PersistenceChannelRunner implements Callable<Void> {

    private final String channelId;
    private final PersistenceContext persistenceContext;
    private final ExecutorCfg executorCfg;
    private final CommentOrderCfg commentOrderCfg;
    private final VideoIteratorCfg videoIteratorCfg;
    private final CommentIteratorCfg commentIteratorCfg;

    public PersistenceChannelRunner(String channelId, PersistenceContext persistenceContext, ExecutorCfg executorCfg, CommentOrderCfg commentOrderCfg, VideoIteratorCfg videoIteratorCfg, CommentIteratorCfg commentIteratorCfg) {
        this.channelId = channelId;
        this.persistenceContext = persistenceContext;
        this.executorCfg = executorCfg;
        this.commentOrderCfg = commentOrderCfg;
        this.videoIteratorCfg = videoIteratorCfg;
        this.commentIteratorCfg = commentIteratorCfg;
    }

    public static ChannelPersistenceRunnerStepBuilder.ExecutorStep newBuilder(String channelId) {
        return ChannelPersistenceRunnerStepBuilder.newBuilder(channelId);
    }

    private Map<String, VideoEntity> grabChannelData(String channelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelDTO channel = collector.call();
        ChannelEntity channelEntity = ChannelEntity.fromChannelDTO(channel);
        List<VideoEntity> videoEntities =
                (videoIteratorCfg.getVideoCountLimit() == VideoIteratorCfg.PROCESS_ALL_VIDEOS ?
                        channel.getVideos().stream() :
                        channel.getVideos().stream().limit(videoIteratorCfg.getVideoCountLimit())
                ).map(v -> VideoEntity.fromVideoDTO(v, channelEntity)).collect(Collectors.toList());
        persistenceContext.commitTransaction(session -> {
            ChannelRepository.saveOrUpdate(channelEntity, session);
            videoEntities.forEach(video -> VideoRepository.saveOrUpdate(video, session));
        });
        return videoEntities.stream().collect(LinkedHashMap::new, (map, video) -> map.put(video.getVideoId(), video), Map::putAll);
    }

    private void grabComments(Map<String, VideoEntity> videoEntityMap) throws InterruptedException {
        executorCfg.addThreadNameSegment(channelId);
        CustomExecutorService executor = executorCfg.build();
        videoEntityMap.keySet().stream()
                .map(videoId -> new PersistenceCommentRunner(videoId, videoEntityMap, persistenceContext, commentOrderCfg, commentIteratorCfg))
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
                .add("channelId='" + channelId + "'")
                .add("persistenceContext=" + persistenceContext)
                .add("executorCfg=" + executorCfg)
                .add("commentOrderCfg=" + commentOrderCfg)
                .add("videoIteratorCfg=" + videoIteratorCfg)
                .add("commentIteratorCfg=" + commentIteratorCfg)
                .toString();
    }

}
