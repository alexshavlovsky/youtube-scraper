package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.http.YoutubeHttpClient;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.ReplyApiResponse;
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
    CommentItemSection fetchNextSection(YoutubeHttpClient youtubeHttpClient, NextContinuationData continuationData) throws ScraperParserException, ScraperHttpException {
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
        return getParentContext().getShortResultStat() + String.format(" > %s replies %s of %s (%.1f%%)", getParentId(), getMeter().getCounter(), getMeter().getTargetCount(), getMeter().getCompletionPercent());
    }
}
