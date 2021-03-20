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

import java.util.List;
import java.util.Map;

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
        getReplyMeter().reset();
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
    public void traverse(CommentVisitor commentVisitor) throws ScrapperInterruptedException {
        traverse(commentVisitor, commentVisitor.getCommentIteratorCfg().getCommentPerVideoLimit());
    }

    @Override
    public void handle(CommentVisitor commentVisitor) throws ScrapperInterruptedException {
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
