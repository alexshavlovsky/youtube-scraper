package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.comments.SectionHeaderDTO;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

import static com.ctzn.youtubescraper.parser.ParserUtil.parseDigitsToInt;

@Log
class CommentContext extends AbstractCommentContext {

    private SectionHeaderDTO commentThreadHeader;

    CommentContext(YoutubeVideoCommentsClient youtubeHttpClient) {
        super(youtubeHttpClient);
        reset(youtubeHttpClient.getInitialCommentSectionContinuation());
        log.fine(() -> youtubeHttpClient.getVideoId() + " total comments count: " + getMeter().getTargetCount());
    }

    private void reset(NextContinuationData continuationData) {
        getMeter().reset();
        nextSection(continuationData);
        commentThreadHeader = getSection().getHeader();
        int totalCommentCount = parseDigitsToInt(commentThreadHeader.getCommentsCountText());
        getMeter().setTargetCount(totalCommentCount);
    }

    void sortNewestFirst() {
        if (getMeter().getTargetCount() != 0) {
            log.fine("Sort newest first");
            reset(commentThreadHeader.getOrderNewestFirst());
        }
    }

    @Override
    CommentItemSection fetchNextSection(YoutubeVideoCommentsClient youtubeHttpClient, NextContinuationData continuationData) throws ScraperParserException, ScraperHttpException, ScrapperInterruptedException {
        return youtubeHttpClient.requestNextCommentSection(continuationData, getMeter());
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

    @Override
    public boolean doInfoLog() {
        return true;
    }
}
