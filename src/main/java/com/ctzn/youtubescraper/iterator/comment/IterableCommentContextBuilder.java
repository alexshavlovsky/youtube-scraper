package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperException;

public class IterableCommentContextBuilder extends IterableCommentContextFactory {

    private final String videoId;
    private final boolean sortNewestCommentsFirst;

    public IterableCommentContextBuilder(String videoId, boolean sortNewestCommentsFirst) {
        this.videoId = videoId;
        this.sortNewestCommentsFirst = sortNewestCommentsFirst;
    }

    public IterableCommentContext build() throws ScraperException {
        return newInstance(videoId, sortNewestCommentsFirst);
    }

    public String getVideoId() {
        return videoId;
    }

    public boolean isSortNewestCommentsFirst() {
        return sortNewestCommentsFirst;
    }
}
