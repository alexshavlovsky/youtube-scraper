package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.config.ExecutorCfg;
import com.ctzn.youtubescraper.core.config.VideoIteratorCfg;

import java.time.Duration;

public class PersistenceChannelRunnerStepBuilder {

    private PersistenceChannelRunnerStepBuilder() {
    }

    public static ExecutorStep newBuilder(String channelId, PersistenceService persistenceService) {
        return new Steps(channelId, persistenceService);
    }

    private static class Steps implements ExecutorStep, CommentOrderStep, VideoIteratorStep, CommentIteratorStep, BuildStep {

        private final String channelId;
        private final PersistenceService persistenceService;

        public Steps(String channelId, PersistenceService persistenceService) {
            this.channelId = channelId;
            this.persistenceService = persistenceService;
        }

        private final ExecutorCfg executorCfg = ExecutorCfg.newInstance();
        private CommentOrderCfg commentOrderCfg = CommentOrderCfg.NEWEST_FIRST;
        private final VideoIteratorCfg videoIteratorCfg = VideoIteratorCfg.newInstance();
        private final CommentIteratorCfg commentIteratorCfg = CommentIteratorCfg.newInstance();

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
        public BuildStep processAllChannelComments() {
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
        public BuildStep processAllComments() {
            return this;
        }

        @Override
        public String getChannelId() {
            return channelId;
        }

        @Override
        public ExecutorCfg getExecutorCfg() {
            return executorCfg;
        }

        @Override
        public CommentOrderCfg getCommentOrderCfg() {
            return commentOrderCfg;
        }

        @Override
        public VideoIteratorCfg getVideoIteratorCfg() {
            return videoIteratorCfg;
        }

        @Override
        public CommentIteratorCfg getCommentIteratorCfg() {
            return commentIteratorCfg;
        }

        @Override
        public PersistenceChannelRunner build() {
            return new PersistenceChannelRunner(channelId, persistenceService, executorCfg, commentOrderCfg, videoIteratorCfg, commentIteratorCfg);
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

        BuildStep processAllChannelComments();

    }

    public interface VideoIteratorStep {

        CommentIteratorStep videoCountLimit(int videoCountLimit);

        CommentIteratorStep processAllChannelVideos();

    }

    public interface CommentIteratorStep {

        BuildStep commentCountLimits(int commentCountPerVideoLimit, int replyThreadCountLimit);

        BuildStep processAllComments();

    }

    public interface BuildStep {

        String getChannelId();

        ExecutorCfg getExecutorCfg();

        CommentOrderCfg getCommentOrderCfg();

        VideoIteratorCfg getVideoIteratorCfg();

        CommentIteratorCfg getCommentIteratorCfg();

        PersistenceChannelRunner build();

    }

}
