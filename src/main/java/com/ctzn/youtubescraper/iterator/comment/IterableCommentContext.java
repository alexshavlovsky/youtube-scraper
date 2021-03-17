package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

public interface IterableCommentContext {

    String getVideoId();

    boolean hasContinuation();

    NextContinuationData getContinuationData();

    void nextSection(NextContinuationData continuationData);

    boolean hasSection();

    CommentItemSection getSection();

    CommentContextMeter getMeter();

    CommentContextMeter getReplyMeter();

    String getShortResultStat();

    void traverse(CommentVisitor commentVisitor) throws ScrapperInterruptedException;

    void handle(CommentVisitor commentVisitor) throws ScrapperInterruptedException;

}
