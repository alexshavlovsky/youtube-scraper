package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.http.useragent.UserAgentAbstractFactory;
import com.ctzn.youtubescraper.http.YoutubeVideoCommentsClient;

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

    static IterableCommentContext newInstance(String videoId, boolean sortNewestCommentsFirst) throws ScraperException {
        return sortNewestCommentsFirst ?
                IterableCommentContextFactory.newNewestCommentsFirstContext(videoId) :
                IterableCommentContextFactory.newTopCommentsFirstContext(videoId);
    }
}
