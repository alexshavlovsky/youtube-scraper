package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.config.ExecutorCfg;
import com.ctzn.youtubescraper.core.config.VideoIteratorCfg;

import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;

public class PersistenceRunnerStepBuilder {

    private PersistenceRunnerStepBuilder() {
    }

    public static ExecutorStep newChannelRunnerBuilder(String channelId, PersistenceService persistenceService) {
        return new ChannelSteps(channelId, persistenceService);
    }

    public static ExecutorStep newVideoListRunnerBuilder(List<String> videoIds, PersistenceService persistenceService) {
        return new VideoSteps(videoIds, persistenceService);
    }

    private static abstract class Steps implements ExecutorStep, CommentOrderStep, VideoIteratorStep, CommentIteratorStep, BuildStep {

        final ExecutorCfg executorCfg = ExecutorCfg.newInstance();
        CommentOrderCfg commentOrderCfg = CommentOrderCfg.NEWEST_FIRST;
        final VideoIteratorCfg videoIteratorCfg = VideoIteratorCfg.newInstance();
        final CommentIteratorCfg commentIteratorCfg = CommentIteratorCfg.newInstance();

        @Override
        public CommentOrderStep defaultExecutor() {
            return this;
        }

        @Override
        public CommentOrderStep withExecutor(int numberOfThreads, Duration timeout) {
            executorCfg.setNumberOfThreads(numberOfThreads);
            executorCfg.setTimeout(timeout);
            return this;
        }

        @Override
        public CommentOrderStep withExecutor(int numberOfThreads, Duration timeout, String threadNamePrefix) {
            executorCfg.setNumberOfThreads(numberOfThreads);
            executorCfg.setTimeout(timeout);
            executorCfg.setThreadNamePrefix(threadNamePrefix);
            return this;
        }

        @Override
        public VideoIteratorStep topCommentsFirst() {
            commentOrderCfg = CommentOrderCfg.TOP_FIRST;
            return this;
        }

        @Override
        public VideoIteratorStep newestCommentsFirst() {
            commentOrderCfg = CommentOrderCfg.NEWEST_FIRST;
            return this;
        }

        @Override
        public BuildStep processAllComments() {
            return this;
        }

        @Override
        public CommentIteratorStep videoCountLimit(int videoCountLimit) {
            videoIteratorCfg.getVideoCountLimit().set(videoCountLimit);
            return this;
        }

        @Override
        public CommentIteratorStep processAllChannelVideos() {
            return this;
        }

        @Override
        public BuildStep commentCountLimits(int commentCountPerVideoLimit, int replyThreadCountLimit) {
            commentIteratorCfg.getCommentPerVideoLimit().set(commentCountPerVideoLimit);
            commentIteratorCfg.getReplyPerCommentLimit().set(replyThreadCountLimit);
            return this;
        }

        @Override
        public BuildStep processWithNoLimits() {
            return this;
        }


        @Override
        public abstract PersistenceRunner build();

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

    private static class ChannelSteps extends Steps {

        private final String channelId;
        private final PersistenceService persistenceService;

        public ChannelSteps(String channelId, PersistenceService persistenceService) {
            this.channelId = channelId;
            this.persistenceService = persistenceService;
        }

        @Override
        public PersistenceRunner build() {
            return new PersistenceChannelRunner(channelId, persistenceService, executorCfg, commentOrderCfg, videoIteratorCfg, commentIteratorCfg);
        }

    }

    private static class VideoSteps extends Steps {

        private final List<String> videoIds;
        private final PersistenceService persistenceService;

        public VideoSteps(List<String> videoIds, PersistenceService persistenceService) {
            this.videoIds = videoIds;
            this.persistenceService = persistenceService;
        }

        @Override
        public PersistenceRunner build() {
            return new PersistenceVideoListRunner(videoIds, persistenceService, executorCfg, commentOrderCfg, videoIteratorCfg, commentIteratorCfg);
        }

    }

    public interface ExecutorStep {

        CommentOrderStep defaultExecutor();

        CommentOrderStep withExecutor(int numberOfThreads, Duration timeout);

        CommentOrderStep withExecutor(int numberOfThreads, Duration timeout, String threadNamePrefix);

    }

    public interface CommentOrderStep {

        VideoIteratorStep topCommentsFirst();

        VideoIteratorStep newestCommentsFirst();

        BuildStep processAllComments();

    }

    public interface VideoIteratorStep {

        CommentIteratorStep videoCountLimit(int videoCountLimit);

        CommentIteratorStep processAllChannelVideos();

    }

    public interface CommentIteratorStep {

        BuildStep commentCountLimits(int commentCountPerVideoLimit, int replyThreadCountLimit);

        BuildStep processWithNoLimits();

    }

    public interface BuildStep {

        PersistenceRunner build();

    }

}
