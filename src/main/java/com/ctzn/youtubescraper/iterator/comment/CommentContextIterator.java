package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Map;

import static java.util.logging.Level.INFO;

@Log
public class CommentContextIterator {

    private final List<CommentHandler> handlers;
    private final int commentCountLimit;
    private final int replyThreadCountLimit;

    public CommentContextIterator(List<CommentHandler> handlers) {
        this.handlers = handlers;
        this.commentCountLimit = 0;
        this.replyThreadCountLimit = 0;
    }

    public CommentContextIterator(List<CommentHandler> handlers, int commentCountLimit, int replyThreadCountLimit) {
        this.handlers = handlers;
        this.commentCountLimit = commentCountLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
    }

    public void traverse(IterableCommentContext context) throws ScrapperInterruptedException {
        traverse(context, commentCountLimit);
    }

    private final static int LOG_PERIOD = 100;
    private int iterationCounter;

    private void traverse(IterableCommentContext context, int limit) throws ScrapperInterruptedException {
        while (true) {
            if (context.hasSection()) handle(context);
            if (limit > 0 && context.getMeter().getCounter() >= limit) return;
            if (Thread.currentThread().isInterrupted())
                throw new ScrapperInterruptedException("Thread has been interrupted");
            if (context.hasContinuation()) context.nextSection(context.getContinuationData());
            else return;
            if (log.getLevel() == INFO && context instanceof CommentContext && ++iterationCounter >= LOG_PERIOD) {
                iterationCounter -= LOG_PERIOD;
                log.info(context::getShortResultStat);
            }
        }
    }

    private void handle(IterableCommentContext context) throws ScrapperInterruptedException {
        CommentItemSection commentItemSection = context.getSection();
        List<CommentDTO> comments = commentItemSection.getComments(context.getVideoId(), context.getParentId());
        boolean doLog = true;
        if (context instanceof CommentReplyContext) handlers.forEach(handler -> handler.handle(comments));
        else {
            Map<String, NextContinuationData> replyContinuationsMap = commentItemSection.getReplyContinuationsMap();
            for (CommentDTO comment : comments) {
                handlers.forEach(handler -> handler.handle(List.of(comment)));
                NextContinuationData replyThreadContinuation = replyContinuationsMap.get(comment.commentId);
                if (Thread.currentThread().isInterrupted()) break;
                if (replyThreadContinuation != null) {
                    traverse(context.newReplyThread(comment, replyThreadContinuation), replyThreadCountLimit);
                    doLog = false;
                }
            }
        }
        if (doLog) log.fine(context::getShortResultStat);
    }
}
