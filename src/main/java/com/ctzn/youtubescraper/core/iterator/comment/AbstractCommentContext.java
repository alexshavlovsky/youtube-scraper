package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.config.CountLimit;
import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.core.iterator.HeartBeatLogger;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import lombok.extern.java.Log;

import static java.util.logging.Level.INFO;

@Log
abstract class AbstractCommentContext implements IterableCommentContext {

    private final CommentContextMeter meter = new CommentContextMeter();
    private final CommentContextMeter replyMeter = new CommentContextMeter();
    private final HeartBeatLogger heartBeatLogger = new HeartBeatLogger(60000);
    private final YoutubeVideoCommentsClient youtubeHttpClient;
    private CommentItemSection section;

    AbstractCommentContext(YoutubeVideoCommentsClient youtubeHttpClient) {
        this.youtubeHttpClient = youtubeHttpClient;
    }

    AbstractCommentContext(AbstractCommentContext commentContext) {
        this.youtubeHttpClient = commentContext.youtubeHttpClient;
    }

    abstract CommentItemSection fetchNextSection(YoutubeVideoCommentsClient youtubeHttpClient, NextContinuationData continuationData) throws ScraperParserException, ScraperHttpException, ScrapperInterruptedException;

    abstract void updateMeters(int itemsCount);

    @Override
    public void nextSection(NextContinuationData continuationData) throws ScraperException {
        section = fetchNextSection(youtubeHttpClient, continuationData);
    }

    @Override
    public boolean hasContinuation() {
        return hasSection() && section.hasContinuation();
    }

    @Override
    public NextContinuationData getContinuationData() {
        if (!hasContinuation()) throw new IllegalStateException("The context hasn't a continuation data");
        return section.getContinuation();
    }

    @Override
    public String getVideoId() {
        return youtubeHttpClient.getVideoId();
    }

    @Override
    public boolean hasSection() {
        return section != null;
    }

    @Override
    public CommentItemSection getSection() {
        return section;
    }

    @Override
    public CommentContextMeter getMeter() {
        return meter;
    }

    @Override
    public CommentContextMeter getReplyMeter() {
        return replyMeter;
    }

    void traverse(CommentVisitor commentVisitor, CountLimit limit) throws ScraperException {
        while (limit.isValueBelowLimit(getMeter().getCounter())) {
            if (Thread.currentThread().isInterrupted())
                throw new ScrapperInterruptedException("Comment thread iterator has been interrupted");
            if (hasSection()) {
                handle(commentVisitor);
                updateMeters(section.countContentPieces());
                log.fine(this::getShortResultStat);
                heartBeatLogger.run(log, INFO, this::getShortResultStat);
            }
            if (hasContinuation()) nextSection(getContinuationData());
            else return;
        }
    }

}
