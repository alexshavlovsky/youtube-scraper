package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.iterator.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.IterableCommentContextFactory;

import java.util.List;

public class CustomCommentRunner extends AbstractCommentRunner {

    private final boolean sortNewestCommentsFirst;
    private final int totalCommentCountLimit;
    private final int replyThreadCountLimit;

    public CustomCommentRunner(String videoId, List<CommentHandler> handlers, boolean sortNewestCommentsFirst) {
        super(videoId, handlers);
        this.sortNewestCommentsFirst = sortNewestCommentsFirst;
        totalCommentCountLimit = 0;
        replyThreadCountLimit = 0;
    }

    public CustomCommentRunner(String videoId, List<CommentHandler> handlers, boolean sortNewestCommentsFirst, int totalCommentCountLimit, int replyThreadCountLimit) {
        super(videoId, handlers);
        this.totalCommentCountLimit = totalCommentCountLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
        this.sortNewestCommentsFirst = sortNewestCommentsFirst;
    }

    @Override
    IterableCommentContext newCommentContext(String videoId) throws ScraperException {
        return sortNewestCommentsFirst ?
                IterableCommentContextFactory.newNewestCommentsFirstContext(videoId) :
                IterableCommentContextFactory.newTopCommentsFirstContext(videoId);
    }

    @Override
    CommentContextIterator newCommentContextIterator(IterableCommentContext commentContext, List<CommentHandler> handlers) {
        return new CommentContextIterator(commentContext, handlers, totalCommentCountLimit, replyThreadCountLimit);
    }
}
