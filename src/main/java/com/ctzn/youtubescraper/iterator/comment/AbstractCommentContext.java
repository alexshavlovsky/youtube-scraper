package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

@Log
abstract class AbstractCommentContext implements IterableCommentContext {

    private final CommentContextMeter meter = new CommentContextMeter();
    private final CommentContextMeter replyMeter = new CommentContextMeter();
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
    public void nextSection(NextContinuationData continuationData) {
        try {
            section = fetchNextSection(youtubeHttpClient, continuationData);
            // The case when API returns an empty section
            // For example if comment has 10 replies and size of reply continuation section is 10 then
            // First continuation section contains 10 replies and the second continuation section is empty so shouldn't we just ignore it
            if (section == null) return;
            updateMeters(section.countContentPieces());
            // TODO maybe it would be better to rethrow the exceptions in some use cases
        } catch (ScraperHttpException | ScraperParserException e) {
            log.warning(youtubeHttpClient.getVideoId() + " " + e.toString());
            section = null;
        } catch (ScrapperInterruptedException e) {
            section = null;
        }
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

    void traverse(CommentIteratorSettings iteratorContext, int limit) throws ScrapperInterruptedException {
        while (true) {
            if (hasSection()) handle(iteratorContext);
            if (limit > 0 && getMeter().getCounter() >= limit) return;
            if (Thread.currentThread().isInterrupted())
                throw new ScrapperInterruptedException("Thread has been interrupted");
            if (hasContinuation()) nextSection(getContinuationData());
            else return;
        }

    }

}
