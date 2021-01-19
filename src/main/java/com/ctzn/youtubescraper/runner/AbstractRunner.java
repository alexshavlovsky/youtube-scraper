package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.iterator.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import lombok.extern.java.Log;

import java.util.List;

@Log
abstract class AbstractRunner implements Runnable {
    private final String videoId;
    private final List<CommentHandler> handlers;

    AbstractRunner(String videoId, List<CommentHandler> handlers) {
        this.videoId = videoId;
        this.handlers = handlers;
    }

    abstract IterableCommentContext newCommentContext(String videoId) throws ScraperException;

    @Override
    public void run() {
        try {
            IterableCommentContext commentContext = newCommentContext(videoId);
            CommentContextIterator iterator = new CommentContextIterator(commentContext, handlers);
            iterator.traverse();
            log.info(videoId + " DONE " + commentContext.getShortResultStat());
        } catch (ScraperException e) {
            log.warning(videoId + " FAILED: " + e.toString());
        }
    }
}
