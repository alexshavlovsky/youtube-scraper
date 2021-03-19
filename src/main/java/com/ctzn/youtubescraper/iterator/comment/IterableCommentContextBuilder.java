package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.config.CommentOrderCfg;

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
