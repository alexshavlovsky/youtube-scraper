package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.http.UserAgentCfg;
import com.ctzn.youtubescraper.http.YoutubeHttpClient;

public class IterableCommentContextFactory {

    private static final UserAgentCfg DEFAULT_USER_AGENT_CONTEXT = new UserAgentCfg(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "en-US,en;q=0.5",
            "gzip, deflate, br"
    );

    private static CommentContext newYoutubeDefaultCommentContext(String videoId) throws ScraperException {
        YoutubeHttpClient youtubeHttpClient = new YoutubeHttpClient(DEFAULT_USER_AGENT_CONTEXT, videoId);
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
