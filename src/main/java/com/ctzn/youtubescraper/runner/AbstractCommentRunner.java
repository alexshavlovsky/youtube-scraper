package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.iterator.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import lombok.extern.java.Log;

import java.util.List;

@Log
abstract class AbstractCommentRunner implements Runnable {
    private final String videoId;
    private final List<CommentHandler> handlers;

    AbstractCommentRunner(String videoId, List<CommentHandler> handlers) {
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
            log.info("DONE " + commentContext.getShortResultStat());
        } catch (ScrapperInterruptedException e) {
            log.info("INTERRUPTED " + videoId + ": " + e.toString());
        } catch (ScraperException e) {
            log.warning("FAILED " + videoId + ": " + e.toString());
        }
    }
}
