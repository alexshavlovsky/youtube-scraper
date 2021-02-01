package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.comments.ReplyApiResponse;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
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
        return youtubeHttpClient.requestNextSection(continuationData, getMeter(), ReplyApiResponse.class);
    }

    @Override
    public String getParentId() {
        return parentComment.commentId;
    }

    @Override
    public AbstractCommentContext getParentContext() {
        return parentCommentContext;
    }

    @Override
    public IterableCommentContext newReplyThread(CommentDTO comment, NextContinuationData replyThreadContinuation) {
        throw new IllegalStateException("Youtube reply can't have child replies");
    }

    @Override
    public String getShortResultStat() {
        CommentContextMeter m = getMeter();
        return getParentContext().getShortResultStat() + String.format(" > %s replies %s of %s%s", getParentId(), m.getCounter(), m.getTargetCount(), m.formatCompletionPercent(" (%.1f%%)"));
    }
}
