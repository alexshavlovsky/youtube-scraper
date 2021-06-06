package com.ctzn.youtubescraper.core.persistence.runner.stepbuilder;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.config.ExecutorCfg;
import com.ctzn.youtubescraper.core.config.VideoIteratorCfg;
import com.ctzn.youtubescraper.core.persistence.PersistenceRunnerStepBuilder;
import com.ctzn.youtubescraper.core.persistence.PersistenceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistenceChannelRunnerStepBuilderTest {

    @Test
    void testUnrestricted() {
        PersistenceRunnerStepBuilder.BuildStep buildStep =
                PersistenceRunnerStepBuilder
                        .newChannelRunnerBuilder("abc123", Mockito.mock(PersistenceService.class))
                        .defaultExecutor()
                        .processAllComments();

        ExecutorCfg executorCfg = buildStep.getExecutorCfg();
        CommentOrderCfg commentOrderCfg = buildStep.getCommentOrderCfg();
        CommentIteratorCfg commentIteratorCfg = buildStep.getCommentIteratorCfg();
        VideoIteratorCfg videoIteratorCfg = buildStep.getVideoIteratorCfg();

        assertEquals(10, executorCfg.getNumberOfThreads());
        assertEquals(Duration.ofDays(1), executorCfg.getTimeout());

        assertTrue(commentOrderCfg.isNewestFirst());

        assertTrue(commentIteratorCfg.getCommentPerVideoLimit().isUnrestricted());
        assertTrue(commentIteratorCfg.getReplyPerCommentLimit().isUnrestricted());

        assertTrue(videoIteratorCfg.getVideoCountLimit().isUnrestricted());
    }

    @Test
    void testComplex() {
        PersistenceRunnerStepBuilder.BuildStep buildStep =
                PersistenceRunnerStepBuilder
                        .newChannelRunnerBuilder("qwerty", Mockito.mock(PersistenceService.class))
                        .withExecutor(17, Duration.ofMinutes(12), "thread123")
                        .topCommentsFirst()
                        .videoCountLimit(5)
                        .commentCountLimits(71, 19);

        ExecutorCfg executorCfg = buildStep.getExecutorCfg();
        CommentOrderCfg commentOrderCfg = buildStep.getCommentOrderCfg();
        CommentIteratorCfg commentIteratorCfg = buildStep.getCommentIteratorCfg();
        VideoIteratorCfg videoIteratorCfg = buildStep.getVideoIteratorCfg();

        assertEquals(17, executorCfg.getNumberOfThreads());
        assertEquals(Duration.ofMinutes(12), executorCfg.getTimeout());
        assertEquals("thread123", executorCfg.getThreadNamePrefix());

        assertTrue(commentOrderCfg.isTopFirst());

        assertEquals(71, commentIteratorCfg.getCommentPerVideoLimit().get());
        assertEquals(19, commentIteratorCfg.getReplyPerCommentLimit().get());

        assertEquals(5, videoIteratorCfg.getVideoCountLimit().get());
    }

}
