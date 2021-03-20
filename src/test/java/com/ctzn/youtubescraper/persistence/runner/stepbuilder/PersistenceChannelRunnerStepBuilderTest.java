package com.ctzn.youtubescraper.persistence.runner.stepbuilder;

import com.ctzn.youtubescraper.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.config.CommentOrderCfg;
import com.ctzn.youtubescraper.config.ExecutorCfg;
import com.ctzn.youtubescraper.config.VideoIteratorCfg;
import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunnerStepBuilder;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistenceChannelRunnerStepBuilderTest {

    @Test
    void testUnrestricted() {
        PersistenceChannelRunnerStepBuilder.BuildStep buildStep =
                PersistenceChannelRunnerStepBuilder
                        .newBuilder("abc123")
                        .defaultExecutor()
                        .processAllChannelComments();

        ExecutorCfg executorCfg = buildStep.getExecutorCfg();
        CommentOrderCfg commentOrderCfg = buildStep.getCommentOrderCfg();
        CommentIteratorCfg commentIteratorCfg = buildStep.getCommentIteratorCfg();
        VideoIteratorCfg videoIteratorCfg = buildStep.getVideoIteratorCfg();

        assertEquals("abc123", buildStep.getChannelId());

        assertEquals(10, executorCfg.getNumberOfThreads());
        assertEquals(Duration.ofDays(1), executorCfg.getTimeout());

        assertTrue(commentOrderCfg.isNewestFirst());

        assertTrue(commentIteratorCfg.getCommentPerVideoLimit().isUnrestricted());
        assertTrue(commentIteratorCfg.getReplyPerCommentLimit().isUnrestricted());

        assertTrue(videoIteratorCfg.getVideoCountLimit().isUnrestricted());
    }

    @Test
    void testComplex() {
        PersistenceChannelRunnerStepBuilder.BuildStep buildStep =
                PersistenceChannelRunnerStepBuilder
                        .newBuilder("qwerty")
                        .withExecutor(17, Duration.ofMinutes(12), "thread123")
                        .topCommentsFirst()
                        .videoCountLimit(5)
                        .commentCountLimits(71, 19);

        ExecutorCfg executorCfg = buildStep.getExecutorCfg();
        CommentOrderCfg commentOrderCfg = buildStep.getCommentOrderCfg();
        CommentIteratorCfg commentIteratorCfg = buildStep.getCommentIteratorCfg();
        VideoIteratorCfg videoIteratorCfg = buildStep.getVideoIteratorCfg();

        assertEquals("qwerty", buildStep.getChannelId());

        assertEquals(17, executorCfg.getNumberOfThreads());
        assertEquals(Duration.ofMinutes(12), executorCfg.getTimeout());
        assertEquals("thread123", executorCfg.getThreadNamePrefix());

        assertTrue(commentOrderCfg.isTopFirst());

        assertEquals(71, commentIteratorCfg.getCommentPerVideoLimit().get());
        assertEquals(19, commentIteratorCfg.getReplyPerCommentLimit().get());

        assertEquals(5, videoIteratorCfg.getVideoCountLimit().get());
    }

}
