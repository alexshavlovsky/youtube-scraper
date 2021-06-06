package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;

public interface IterableCommentContext {

    String getVideoId();

    boolean hasContinuation();

    NextContinuationData getContinuationData();

    void nextSection(NextContinuationData continuationData) throws ScraperException;

    boolean hasSection();

    CommentItemSection getSection();

    CommentContextMeter getMeter();

    CommentContextMeter getReplyMeter();

    default int getTotalCommentCount() {
        return getMeter().getTargetCount();
    }

    String getShortResultStat();

    void traverse(CommentVisitor commentVisitor) throws ScraperException;

    void handle(CommentVisitor commentVisitor) throws ScraperException;

}
