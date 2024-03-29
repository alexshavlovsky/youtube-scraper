package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.http.YoutubeVideoCommentsClient;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentAbstractFactory;

class IterableCommentContextFactory {

    private static CommentContext newYoutubeDefaultCommentContext(String videoId) throws ScraperException {
        YoutubeVideoCommentsClient youtubeHttpClient = new YoutubeVideoCommentsClient(UserAgentAbstractFactory.getRandomAgentFactory(), videoId);
        return new CommentContext(youtubeHttpClient);
    }

    private static IterableCommentContext newTopCommentsFirstContext(String videoId) throws ScraperException {
        return newYoutubeDefaultCommentContext(videoId);
    }

    private static IterableCommentContext newNewestCommentsFirstContext(String videoId) throws ScraperException {
        CommentContext context = newYoutubeDefaultCommentContext(videoId);
        context.sortNewestFirst();
        return context;
    }

    static IterableCommentContext newInstance(String videoId, CommentOrderCfg commentOrderCfg) throws ScraperException {
        return commentOrderCfg.isNewestFirst() ?
                IterableCommentContextFactory.newNewestCommentsFirstContext(videoId) :
                IterableCommentContextFactory.newTopCommentsFirstContext(videoId);
    }
}
