package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;

public interface IterableCommentContext {

    String getVideoId();

    boolean hasContinuation();

    NextContinuationData getContinuationData();

    void nextSection(NextContinuationData continuationData);

    boolean hasSection();

    CommentItemSection getSection();

    CommentContextMeter getMeter();

    CommentContextMeter getReplyMeter();

    default int getTotalCommentCount() {
        return getMeter().getTargetCount();
    }

    String getShortResultStat();

    void traverse(CommentVisitor commentVisitor) throws ScrapperInterruptedException;

    void handle(CommentVisitor commentVisitor) throws ScrapperInterruptedException;

}
