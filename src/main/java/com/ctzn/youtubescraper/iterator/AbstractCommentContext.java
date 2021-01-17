package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.http.YoutubeHttpClient;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

@Log
abstract class AbstractCommentContext implements IterableCommentContext {

    private final CommentContextMeter meter = new CommentContextMeter();
    private final CommentContextMeter replyMeter = new CommentContextMeter();
    private final YoutubeHttpClient youtubeHttpClient;
    private CommentItemSection section;

    AbstractCommentContext(YoutubeHttpClient youtubeHttpClient) {
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
        return section.nextContinuation();
    }

    abstract CommentItemSection fetchNextSection(YoutubeHttpClient youtubeHttpClient, NextContinuationData continuationData) throws Exception;

    @Override
    public void nextSection(NextContinuationData continuationData) {
        try {
            section = fetchNextSection(youtubeHttpClient, continuationData);
            // The case when API returns an empty section
            // For example if comment has 10 replies and size of reply continuation section is 10 then
            // First continuation section contains 10 replies and the second continuation section is empty so shouldn't we just ignore it
            if (section == null) return;
            meter.incContinuation();
            meter.add(section.countContentPieces());
        } catch (Exception e) {
            e.printStackTrace();
            log.warning(e.toString());
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
}
