package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.iterator.comment.CommentVisitor;
import com.ctzn.youtubescraper.core.iterator.comment.IterableCommentContext;
import com.ctzn.youtubescraper.core.iterator.comment.IterableCommentContextBuilder;
import lombok.extern.java.Log;

import java.util.function.Consumer;

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
            call(null);
        } catch (ScraperException e) {
        }
    }

    public int call(Consumer<Integer> earlyTotalCommentCountConsumer) throws ScraperException {
        try {
            IterableCommentContext context = contextBuilder.build();
            if (earlyTotalCommentCountConsumer!=null) earlyTotalCommentCountConsumer.accept(context.getTotalCommentCount());
            context.traverse(iteratorContext);
            log.info("DONE " + context.getShortResultStat());
            return context.getTotalCommentCount();
        } catch (ScrapperInterruptedException e) {
            log.info("INTERRUPTED " + contextBuilder.getVideoId() + ": " + e.toString());
            throw e;
        } catch (ScraperException e) {
            log.warning("FAILED " + contextBuilder.getVideoId() + ": " + e.toString());
            throw e;
        }
    }

}
