package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

import java.util.List;

public interface IterableCommentContext {

    String getVideoId();

    String getParentId();

    AbstractCommentContext getParentContext();

    boolean hasContinuation();

    NextContinuationData getContinuationData();

    void nextSection(NextContinuationData continuationData);

    boolean hasSection();

    CommentItemSection getSection();

    CommentContextMeter getMeter();

    CommentContextMeter getReplyMeter();

    IterableCommentContext newReplyThread(CommentDTO comment, NextContinuationData replyThreadContinuation);

    String getShortResultStat();

    boolean doInfoLog();

    default List<CommentDTO> getComments() {
        return getSection().getComments(getVideoId(), getParentId());
    }

}
