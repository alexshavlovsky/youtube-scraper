package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
import lombok.extern.java.Log;

@Log
class CommentReplyContext extends AbstractCommentContext {

    private final CommentContext parentCommentContext;
    private final CommentDTO parentComment;

    CommentReplyContext(CommentContext parentCommentContext, CommentDTO parentComment, NextContinuationData replyThreadContinuation) {
        super(parentCommentContext);
        this.parentCommentContext = parentCommentContext;
        this.parentComment = parentComment;
        getMeter().setTargetCount(parentComment.replyCount);
        nextSection(replyThreadContinuation);
    }

    @Override
    CommentItemSection fetchNextSection(YoutubeVideoCommentsClient youtubeHttpClient, NextContinuationData continuationData) throws ScraperParserException, ScraperHttpException, ScrapperInterruptedException {
        return youtubeHttpClient.requestNextReplySection(continuationData, getMeter());
    }

    @Override
    void updateMeters(int itemsCount) {
        getMeter().update(itemsCount);
        parentCommentContext.getMeter().update(itemsCount);
        parentCommentContext.getReplyMeter().update(itemsCount);
    }

    @Override
    public String getShortResultStat() {
        CommentContextMeter m = getMeter();
        return parentCommentContext.getShortResultStat() +
                String.format(" > %s #%s replies %s of %s%s", parentComment.commentId, m.getContinuationCounter(), m.getCounter(), m.getTargetCount(), m.formatCompletionString());
    }

    @Override
    public void traverse(CommentVisitor commentVisitor) throws ScrapperInterruptedException {
        traverse(commentVisitor, commentVisitor.getCommentIteratorCfg().getReplyPerCommentLimit());
    }

    @Override
    public void handle(CommentVisitor commentVisitor) {
        commentVisitor.visitAll(getSection().getComments(getVideoId(), parentComment.commentId));
    }

}
