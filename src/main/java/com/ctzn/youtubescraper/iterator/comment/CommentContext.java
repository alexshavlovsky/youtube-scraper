package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.iterator.HeartBeatLogger;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.comments.SectionHeaderDTO;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Map;

import static com.ctzn.youtubescraper.parser.ParserUtil.parseDigitsToInt;
import static java.util.logging.Level.INFO;

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
    void updateMeters(int itemsCount) {
        getMeter().update(itemsCount);
    }

    @Override
    public String getShortResultStat() {
        CommentContextMeter meter = getMeter();
        CommentContextMeter replyMeter = getReplyMeter();
        return String.format("%s #%s %s comments, %s replies, total %s of %s%s", getVideoId(), meter.getContinuationCounter(), meter.getCounter() - replyMeter.getCounter(), replyMeter.getCounter(), meter.getCounter(), meter.getTargetCount(), meter.formatCompletionString());
    }

    @Override
    public void traverse(CommentIteratorSettings iteratorSettings) throws ScrapperInterruptedException {
        traverse(iteratorSettings, iteratorSettings.getCommentCountLimit());
    }

    private final HeartBeatLogger heartBeatLogger = new HeartBeatLogger(60000);

    @Override
    public void handle(CommentIteratorSettings iteratorSettings) throws ScrapperInterruptedException {
        List<CommentDTO> comments = getSection().getComments(getVideoId(), null);
        Map<String, NextContinuationData> replyContinuationsMap = getSection().getReplyContinuationsMap();
        boolean doLog = true;
        for (CommentDTO comment : comments) {
            iteratorSettings.getHandler().accept(List.of(comment));
            NextContinuationData replyThreadContinuation = replyContinuationsMap.get(comment.commentId);
            if (Thread.currentThread().isInterrupted()) break;
            if (replyThreadContinuation != null) {
                new CommentReplyContext(this, comment, replyThreadContinuation).traverse(iteratorSettings);
                doLog = false;
            }
        }
        if (doLog) log.fine(this::getShortResultStat);
        heartBeatLogger.run(log, INFO, this::getShortResultStat);
    }

}
