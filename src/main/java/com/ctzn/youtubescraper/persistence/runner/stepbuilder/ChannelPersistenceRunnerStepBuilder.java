package com.ctzn.youtubescraper.persistence.runner.stepbuilder;

import com.ctzn.youtubescraper.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.config.CommentOrderCfg;
import com.ctzn.youtubescraper.config.ExecutorCfg;
import com.ctzn.youtubescraper.config.VideoIteratorCfg;
import com.ctzn.youtubescraper.persistence.DefaultPersistenceContext;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.time.Duration;

public class ChannelPersistenceRunnerStepBuilder {

    private ChannelPersistenceRunnerStepBuilder() {
    }

    public static ExecutorStep newBuilder(String channelId) {
        return new Steps(channelId);
    }

    private static class Steps implements ExecutorStep, CommentOrderStep, VideoIteratorStep, CommentIteratorStep, BuildStep {

        private final String channelId;
        private final PersistenceContext persistenceContext = new DefaultPersistenceContext();

        Steps(String channelId) {
            this.channelId = channelId;
        }

        private ExecutorCfg executorCfg = ExecutorCfg.newInstance();
        private CommentOrderCfg commentOrderCfg = new CommentOrderCfg();
        private VideoIteratorCfg videoIteratorCfg = new VideoIteratorCfg();
        private CommentIteratorCfg commentIteratorCfg = CommentIteratorCfg.newInstance();

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
            commentOrderCfg.setCommentOrder(CommentOrderCfg.CommentOrder.TOP_FIRST);
            return this;
        }

        @Override
        public VideoIteratorStep newestCommentsFirst() {
            commentOrderCfg.setCommentOrder(CommentOrderCfg.CommentOrder.NEWEST_FIRST);
            return this;
        }

        @Override
        public BuildStep processAllChannelComments() {
            commentOrderCfg = new CommentOrderCfg();
            return this;
        }

        @Override
        public CommentIteratorStep videoCountLimit(int videoCountLimit) {
            videoIteratorCfg.setVideoCountLimit(videoCountLimit);
            return this;
        }

        @Override
        public CommentIteratorStep processAllChannelVideos() {
            return this;
        }

        @Override
        public BuildStep commentCountLimits(int commentCountPerVideoLimit, int replyThreadCountLimit) {
            commentIteratorCfg.setCommentCountPerVideoLimit(commentCountPerVideoLimit);
            commentIteratorCfg.setReplyThreadCountLimit(replyThreadCountLimit);
            return this;
        }

        @Override
        public BuildStep processAllComments() {
            return this;
        }

        @Override
        public PersistenceChannelRunner build() {
            return new PersistenceChannelRunner(channelId, persistenceContext, executorCfg, commentOrderCfg, videoIteratorCfg, commentIteratorCfg);
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

        PersistenceChannelRunner build();

    }


}
