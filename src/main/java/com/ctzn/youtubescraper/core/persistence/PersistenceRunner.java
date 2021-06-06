package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.config.ExecutorCfg;
import com.ctzn.youtubescraper.core.config.VideoIteratorCfg;
import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.executor.CustomExecutorService;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelVideosDTO;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
import lombok.extern.java.Log;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Log
public abstract class PersistenceRunner implements Callable<Void> {

    public static PersistenceRunnerStepBuilder.ExecutorStep newChannelRunnerBuilder(String channelId, PersistenceService persistenceService) {
        return PersistenceRunnerStepBuilder.newChannelRunnerBuilder(channelId, persistenceService);
    }

    public static PersistenceRunnerStepBuilder.ExecutorStep newVideoListRunnerBuilder(List<String> videoIds, PersistenceService persistenceService) {
        return PersistenceRunnerStepBuilder.newVideoListRunnerBuilder(videoIds, persistenceService);
    }

    final PersistenceService persistenceService;
    private final ExecutorCfg executorCfg;
    private final CommentOrderCfg commentOrderCfg;
    private final VideoIteratorCfg videoIteratorCfg;
    private final CommentIteratorCfg commentIteratorCfg;

    public PersistenceRunner(PersistenceService persistenceService, ExecutorCfg executorCfg, CommentOrderCfg commentOrderCfg, VideoIteratorCfg videoIteratorCfg, CommentIteratorCfg commentIteratorCfg) {
        this.persistenceService = persistenceService;
        this.executorCfg = executorCfg;
        this.commentOrderCfg = commentOrderCfg;
        this.videoIteratorCfg = videoIteratorCfg;
        this.commentIteratorCfg = commentIteratorCfg;
    }

    List<VideoDTO> grabChannelData(String channelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelVideosDTO channelVideos = collector.call();
        persistenceService.saveChannelVideos(channelVideos);
        return (videoIteratorCfg.getVideoCountLimit().isUnrestricted() ?
                channelVideos.getVideos().stream() :
                channelVideos.getVideos().stream().limit(videoIteratorCfg.getVideoCountLimit().get())
        ).collect(Collectors.toList());
    }

    void grabComments(List<VideoDTO> videos, String threadNamePrefix) throws InterruptedException {
        executorCfg.addThreadNameSegment(threadNamePrefix);
        CustomExecutorService executor = executorCfg.build();
        videos.stream()
                .map(video -> new PersistenceCommentRunner(video.getVideoId(), persistenceService, commentOrderCfg, commentIteratorCfg))
                .forEach(executor::submit);
        executor.awaitAndTerminate();
    }

    @Override
    public abstract Void call() throws Exception;

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add(executorCfg.toString())
                .add(commentOrderCfg.toString())
                .add(videoIteratorCfg.toString())
                .add(commentIteratorCfg.toString())
                .toString();
    }

}
