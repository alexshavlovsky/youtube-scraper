package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.iterator.HeartBeatLogger;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Map;

import static java.util.logging.Level.INFO;

@Log
public class CommentContextIterator {

    private final DataHandler<CommentDTO> handler;
    private final int commentCountLimit;
    private final int replyThreadCountLimit;

    public CommentContextIterator(DataHandler<CommentDTO> handler) {
        this.handler = handler;
        this.commentCountLimit = 0;
        this.replyThreadCountLimit = 0;
    }

    public CommentContextIterator(DataHandler<CommentDTO> handler, int commentCountLimit, int replyThreadCountLimit) {
        this.handler = handler;
        this.commentCountLimit = commentCountLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
    }

    public void traverse(IterableCommentContext context) throws ScrapperInterruptedException {
        traverse(context, commentCountLimit);
    }

    private final HeartBeatLogger heartBeatLogger = new HeartBeatLogger(60000);

    private void traverse(IterableCommentContext context, int limit) throws ScrapperInterruptedException {
        while (true) {
            if (context.hasSection()) handle(context);
            if (limit > 0 && context.getMeter().getCounter() >= limit) return;
            if (Thread.currentThread().isInterrupted())
                throw new ScrapperInterruptedException("Thread has been interrupted");
            if (context.hasContinuation()) context.nextSection(context.getContinuationData());
            else return;
            if (context.doInfoLog()) heartBeatLogger.run(log, INFO, context::getShortResultStat);
        }
    }

    private void handle(IterableCommentContext context) throws ScrapperInterruptedException {
        List<CommentDTO> comments = context.getComments();
        boolean doLog = true;
        if (context instanceof CommentReplyContext) handler.accept(comments);
        else {
            Map<String, NextContinuationData> replyContinuationsMap = context.getSection().getReplyContinuationsMap();
            for (CommentDTO comment : comments) {
                handler.accept(List.of(comment));
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
