package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.http.YoutubeHttpClient;
import com.ctzn.youtubescraper.model.CommentApiResponse;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.SectionHeaderDTO;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.parser.CommentThreadHeaderParser;
import lombok.extern.java.Log;

@Log
class CommentContext extends AbstractCommentContext {

    private static final CommentThreadHeaderParser commentThreadHeaderParser = new CommentThreadHeaderParser();
    private SectionHeaderDTO commentThreadHeader;

    CommentContext(YoutubeHttpClient youtubeHttpClient) {
        super(youtubeHttpClient);
        reset(youtubeHttpClient.getInitialCommentSectionContinuation());
        log.info(() -> youtubeHttpClient.getVideoId() + " total comments count: " + getMeter().getTargetCounter());
    }

    private void reset(NextContinuationData continuationData) {
        getMeter().reset();
        nextSection(continuationData);
        commentThreadHeader = getSection().getHeader();
        int totalCommentCount = commentThreadHeaderParser.parseCommentsCountText(commentThreadHeader.getCommentsCountText());
        getMeter().setTargetCounter(totalCommentCount);
    }

    void sortNewestFirst() {
        if (getMeter().getTargetCounter() != 0) reset(commentThreadHeader.getOrderNewestFirst());
    }

    @Override
    CommentItemSection fetchNextSection(YoutubeHttpClient youtubeHttpClient, NextContinuationData continuationData) throws Exception {
        return youtubeHttpClient.requestNextSection(continuationData, getMeter(), CommentApiResponse.class);
    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public AbstractCommentContext getParentContext() {
        return null;
    }

    @Override
    public IterableCommentContext newReplyThread(CommentDTO comment, NextContinuationData replyThreadContinuation) {
        return new CommentReplyContext(this, comment, replyThreadContinuation);
    }
}
