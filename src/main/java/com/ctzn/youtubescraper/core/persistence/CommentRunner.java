package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.iterator.comment.CommentVisitor;
import com.ctzn.youtubescraper.core.iterator.comment.IterableCommentContext;
import com.ctzn.youtubescraper.core.iterator.comment.IterableCommentContextBuilder;
import lombok.extern.java.Log;

@Log
public class CommentRunner implements Runnable {

    private final IterableCommentContextBuilder contextBuilder;
    private final CommentVisitor iteratorContext;

    public CommentRunner(IterableCommentContextBuilder contextBuilder, CommentVisitor iteratorContext) {
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
