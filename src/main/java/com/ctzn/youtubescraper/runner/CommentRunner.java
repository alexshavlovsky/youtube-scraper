package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.iterator.comment.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContextBuilder;
import lombok.extern.java.Log;

@Log
class CommentRunner implements Runnable {

    private final IterableCommentContextBuilder contextBuilder;
    private final CommentContextIterator iterator;

    CommentRunner(IterableCommentContextBuilder contextBuilder, CommentContextIterator iterator) {
        this.contextBuilder = contextBuilder;
        this.iterator = iterator;
    }

    @Override
    public void run() {
        try {
            IterableCommentContext context = contextBuilder.build();
            iterator.traverse(context);
            log.info("DONE " + context.getShortResultStat());
        } catch (ScrapperInterruptedException e) {
            log.info("INTERRUPTED " + contextBuilder.getVideoId() + ": " + e.toString());
        } catch (ScraperException e) {
            log.warning("FAILED " + contextBuilder.getVideoId() + ": " + e.toString());
        }
    }
}
