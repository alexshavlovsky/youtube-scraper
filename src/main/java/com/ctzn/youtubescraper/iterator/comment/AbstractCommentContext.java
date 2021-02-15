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

    @Override
    public boolean hasContinuation() {
        return hasSection() && section.hasContinuation();
    }

    @Override
    public NextContinuationData getContinuationData() {
        if (!hasContinuation()) throw new IllegalStateException("The context hasn't a continuation data");
        return section.getContinuation();
    }

    abstract CommentItemSection fetchNextSection(YoutubeVideoCommentsClient youtubeHttpClient, NextContinuationData continuationData) throws ScraperParserException, ScraperHttpException, ScrapperInterruptedException;

    @Override
    public void nextSection(NextContinuationData continuationData) {
        try {
            section = fetchNextSection(youtubeHttpClient, continuationData);
            // The case when API returns an empty section
            // For example if comment has 10 replies and size of reply continuation section is 10 then
            // First continuation section contains 10 replies and the second continuation section is empty so shouldn't we just ignore it
            // TODO: sometimes API returns comment continuation in response to a reply continuation request. Maybe it's a youtube bug
            // it also results with a section == null
            if (section == null) return;
            // update meters
            int itemsCount = section.countContentPieces();
            meter.update(itemsCount);
            if (getParentContext() != null) {
                getParentContext().getMeter().update(itemsCount);
                getParentContext().getReplyMeter().update(itemsCount);
            }
            // TODO maybe it would be better to rethrow the exceptions in some use cases
        } catch (ScraperHttpException | ScraperParserException e) {
            log.warning(youtubeHttpClient.getVideoId() + " " + e.toString());
            section = null;
        } catch (ScrapperInterruptedException e) {
            section = null;
        }
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

    @Override
    public String getVideoId() {
        return youtubeHttpClient.getVideoId();
    }

    @Override
    public String getShortResultStat() {
        return String.format("%s %s comments, %s replies, total %s of %s%s", getVideoId(), meter.getCounter() - replyMeter.getCounter(), replyMeter.getCounter(), meter.getCounter(), meter.getTargetCount(), meter.formatCompletionPercent(" (%.1f%%)"));
    }
}
