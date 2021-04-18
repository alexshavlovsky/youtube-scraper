package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.config.ExecutorCfg;
import com.ctzn.youtubescraper.core.config.VideoIteratorCfg;
import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.executor.CustomExecutorService;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelVideosDTO;
import com.ctzn.youtubescraper.core.persistence.dto.StatusCode;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PersistenceChannelRunner implements Callable<Void> {

    private final String channelId;
    private final PersistenceService persistenceService;
    private final ExecutorCfg executorCfg;
    private final CommentOrderCfg commentOrderCfg;
    private final VideoIteratorCfg videoIteratorCfg;
    private final CommentIteratorCfg commentIteratorCfg;

    public PersistenceChannelRunner(String channelId, PersistenceService persistenceService, ExecutorCfg executorCfg, CommentOrderCfg commentOrderCfg, VideoIteratorCfg videoIteratorCfg, CommentIteratorCfg commentIteratorCfg) {
        this.channelId = channelId;
        this.persistenceService = persistenceService;
        this.executorCfg = executorCfg;
        this.commentOrderCfg = commentOrderCfg;
        this.videoIteratorCfg = videoIteratorCfg;
        this.commentIteratorCfg = commentIteratorCfg;
    }

    public static PersistenceChannelRunnerStepBuilder.ExecutorStep newBuilder(String channelId, PersistenceService persistenceService) {
        return PersistenceChannelRunnerStepBuilder.newBuilder(channelId, persistenceService);
    }

    private List<VideoDTO> grabChannelData(String channelId) throws ScraperException {
        ChannelVideosCollector collector = new ChannelVideosCollector(channelId);
        ChannelVideosDTO channelVideos = collector.call();
        persistenceService.saveChannelVideos(channelVideos);
        return (videoIteratorCfg.getVideoCountLimit().isUnrestricted() ?
                channelVideos.getVideos().stream() :
                channelVideos.getVideos().stream().limit(videoIteratorCfg.getVideoCountLimit().get())
        ).collect(Collectors.toList());
    }

    private void grabComments(List<VideoDTO> videos) throws InterruptedException {
        executorCfg.addThreadNameSegment(channelId);
        CustomExecutorService executor = executorCfg.build();
        videos.stream()
                .map(video -> new PersistenceCommentRunner(video.getVideoId(), persistenceService, commentOrderCfg, commentIteratorCfg))
                .forEach(executor::submit);
        executor.awaitAndTerminate();
    }

    @Override
    public Void call() throws Exception {
        persistenceService.logChannel(channelId, StatusCode.PASSED_TO_WORKER, toString());
        try {
            List<VideoDTO> videos = grabChannelData(channelId);
            grabComments(videos);
            persistenceService.logChannel(channelId, StatusCode.DONE, "Videos processed: " + videos.size());
        } catch (Exception e) {
            persistenceService.logChannel(channelId, StatusCode.ERROR, e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("channelId='" + channelId + "'")
                .add(executorCfg.toString())
                .add(commentOrderCfg.toString())
                .add(videoIteratorCfg.toString())
                .add(commentIteratorCfg.toString())
                .toString();
    }

}
