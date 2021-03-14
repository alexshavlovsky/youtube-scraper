package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.iterator.comment.CommentIteratorSettings;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContextBuilder;
import lombok.extern.java.Log;

@Log
class CommentRunner implements Runnable {

    private final IterableCommentContextBuilder contextBuilder;
    private final CommentIteratorSettings iteratorContext;

    public CommentRunner(IterableCommentContextBuilder contextBuilder, CommentIteratorSettings iteratorContext) {
        this.contextBuilder = contextBuilder;
        this.iteratorContext = iteratorContext;
    }

    @Override
    public void run() {
        try {
            IterableCommentContext context = contextBuilder.build();
            context.traverse(iteratorContext);
            log.info("DONE " + context.getShortResultStat());
        } catch (ScrapperInterruptedException e) {
            log.info("INTERRUPTED " + contextBuilder.getVideoId() + ": " + e.toString());
        } catch (ScraperException e) {
            log.warning("FAILED " + contextBuilder.getVideoId() + ": " + e.toString());
        }
    }
}
