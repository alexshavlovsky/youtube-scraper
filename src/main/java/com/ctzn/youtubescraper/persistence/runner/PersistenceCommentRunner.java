package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.runner.CustomCommentRunner;

import java.util.List;
import java.util.Map;

class PersistenceCommentRunner extends AbstractPersistenceCommentRunner {

    private final boolean sortNewestCommentsFirst;
    private final int totalCommentCountLimit;
    private final int replyThreadCountLimit;

    PersistenceCommentRunner(String videoId, Map<String, VideoEntity> videoEntityMap, PersistenceContext persistenceContext, boolean sortNewestCommentsFirst, int totalCommentCountLimit, int replyThreadCountLimit) {
        super(videoId, videoEntityMap, persistenceContext);
        this.sortNewestCommentsFirst = sortNewestCommentsFirst;
        this.totalCommentCountLimit = totalCommentCountLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
    }

    @Override
    Runnable newCommentRunner(String videoId, List<CommentHandler> handlers) {
        return new CustomCommentRunner(videoId, handlers, sortNewestCommentsFirst, totalCommentCountLimit, replyThreadCountLimit);
    }
}
