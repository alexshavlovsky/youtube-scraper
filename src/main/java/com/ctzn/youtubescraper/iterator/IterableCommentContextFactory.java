package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.http.UserAgentCfgFactory;
import com.ctzn.youtubescraper.http.YoutubeVideoCommentsClient;

public class IterableCommentContextFactory {

    private static CommentContext newYoutubeDefaultCommentContext(String videoId) throws ScraperException {
        YoutubeVideoCommentsClient youtubeHttpClient = new YoutubeVideoCommentsClient(UserAgentCfgFactory.getDefaultUserAgentCfg(), videoId);
        return new CommentContext(youtubeHttpClient);
    }

    public static IterableCommentContext newTopCommentsFirstContext(String videoId) throws ScraperException {
        return newYoutubeDefaultCommentContext(videoId);
    }

    public static IterableCommentContext newNewestCommentsFirstContext(String videoId) throws ScraperException {
        CommentContext context = newYoutubeDefaultCommentContext(videoId);
        context.sortNewestFirst();
        return context;
    }
}
