package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.handler.AbstractHandler;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Map;

@Log
public class CommentContextIterator {

    private final IterableCommentContext context;
    private final List<CommentHandler> handlers;

    public CommentContextIterator(IterableCommentContext context, List<CommentHandler> handlers) {
        this.context = context;
        this.handlers = handlers;
    }

    public void traverse() {
        traverse(context);
    }

    private void traverse(IterableCommentContext context) {
        while (true) {
            if (context.hasSection()) handle(context);
            if (context.hasContinuation()) context.nextSection(context.getContinuationData());
            else return;
        }
    }

    private void handle(IterableCommentContext context) {
        CommentItemSection commentItemSection = context.getSection();
        List<CommentDTO> comments = commentItemSection.getComments(context.getVideoId(), context.getParentId());
        if (context instanceof CommentReplyContext) handlers.forEach(handler -> handler.handle(comments));
        else {
            Map<String, NextContinuationData> replyContinuationsMap = commentItemSection.getReplyContinuationsMap();
            for (CommentDTO comment : comments) {
                handlers.forEach(handler -> handler.handle(List.of(comment)));
                NextContinuationData replyThreadContinuation = replyContinuationsMap.get(comment.commentId);
                if (replyThreadContinuation != null) traverse(context.newReplyThread(comment, replyThreadContinuation));
            }
        }
        log.fine(context.getShortResultStat());
    }
}
