package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.core.model.comments.SectionHeaderDTO;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Map;

import static com.ctzn.youtubescraper.core.parser.ParserUtil.parseDigitsToInt;

@Log
class CommentContext extends AbstractCommentContext {

    private SectionHeaderDTO commentThreadHeader;

    CommentContext(YoutubeVideoCommentsClient youtubeHttpClient) throws ScraperException {
        super(youtubeHttpClient);
        reset(youtubeHttpClient.getInitialCommentSectionContinuation());
        log.fine(() -> youtubeHttpClient.getVideoId() + " total comments count: " + getMeter().getTargetCount());
    }

    private void reset(NextContinuationData continuationData) throws ScraperException {
        getMeter().reset();
        getReplyMeter().reset();
        nextSection(continuationData);
        commentThreadHeader = getSection().getHeader();
        int totalCommentCount = parseDigitsToInt(commentThreadHeader.getCommentsCountText());
        getMeter().setTargetCount(totalCommentCount);
    }

    void sortNewestFirst() throws ScraperException {
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
    void updateMeters(int itemsCount) {
        getMeter().update(itemsCount);
    }

    @Override
    public String getShortResultStat() {
        CommentContextMeter m = getMeter();
        CommentContextMeter rm = getReplyMeter();
        return String.format("%s #%s %s comments, %s replies, total %s of %s%s", getVideoId(), m.getContinuationCounter(), m.getCounter() - rm.getCounter(), rm.getCounter(), m.getCounter(), m.getTargetCount(), m.formatCompletionString());
    }

    @Override
    public void traverse(CommentVisitor commentVisitor) throws ScraperException {
        traverse(commentVisitor, commentVisitor.getCommentIteratorCfg().getCommentPerVideoLimit());
    }

    @Override
    public void handle(CommentVisitor commentVisitor) throws ScraperException {
        List<CommentDTO> comments = getSection().getComments(getVideoId(), null);
        if (commentVisitor.getCommentIteratorCfg().getReplyPerCommentLimit().isLimitedToZero())
            commentVisitor.visitAll(comments);
        else {
            Map<String, NextContinuationData> replyContinuationMap = getSection().getReplyContinuationMap();
            for (CommentDTO comment : comments)
                if (!Thread.currentThread().isInterrupted()) {
                    commentVisitor.visit(comment);
                    NextContinuationData replyThreadContinuation = replyContinuationMap.get(comment.commentId);
                    if (replyThreadContinuation != null)
                        new CommentReplyContext(this, comment, replyThreadContinuation).traverse(commentVisitor);
                }
        }
    }

}
