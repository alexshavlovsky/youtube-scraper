package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.IterableCommentContextFactory;

import java.util.List;

public class CommentNewestFirstRunner extends AbstractCommentRunner {

    public CommentNewestFirstRunner(String videoId, List<CommentHandler> handlers) {
        super(videoId, handlers);
    }

    @Override
    IterableCommentContext newCommentContext(String videoId) throws ScraperException {
        return IterableCommentContextFactory.newNewestCommentsFirstContext(videoId);
    }
}
