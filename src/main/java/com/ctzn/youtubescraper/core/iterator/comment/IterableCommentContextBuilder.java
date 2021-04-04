package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.exception.ScraperException;

public class IterableCommentContextBuilder extends IterableCommentContextFactory {

    private final String videoId;
    private final CommentOrderCfg commentOrderCfg;

    public IterableCommentContextBuilder(String videoId, CommentOrderCfg commentOrderCfg) {
        this.videoId = videoId;
        this.commentOrderCfg = commentOrderCfg;
    }

    public IterableCommentContext build() throws ScraperException {
        return newInstance(videoId, commentOrderCfg);
    }

    public String getVideoId() {
        return videoId;
    }

}
