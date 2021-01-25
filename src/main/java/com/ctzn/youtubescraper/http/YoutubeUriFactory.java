package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.model.commons.NextContinuationData;

import java.net.URI;

import static com.ctzn.youtubescraper.http.IoUtil.joinQueryParamsOrdered;

class YoutubeUriFactory {

    private final static String CHANNEL_PAGE_URI_TEMPLATE = "https://www.youtube.com/channel/%s";
    private final static String CHANNEL_VIDEOS_PAGE_URI_TEMPLATE = "https://www.youtube.com/%s/videos";
    private final static String VIDEO_PAGE_URI_TEMPLATE = "https://www.youtube.com/watch?v=%s";
    private final static String COMMENT_API_URI_TEMPLATE = "https://www.youtube.com/comment_service_ajax?%s";
    private final static String BROWSE_API_URI_TEMPLATE = "https://www.youtube.com/browse_ajax?%s";

    private static String buildQueryParams(NextContinuationData continuationData) {
        return joinQueryParamsOrdered(
                "action_get_comments", "1",
                "pbj", "1",
                "ctoken", continuationData.getContinuation(),
                // TODO try to omit the "continuation" query parameter
                // in fact this parameter seems to be optional
                // and omitting it allows you to reduce request uri length
                "continuation", continuationData.getContinuation(),
                "itct", continuationData.getClickTrackingParams()
        );
    }

    private static String buildBrowseQueryParams(NextContinuationData continuationData) {
        return joinQueryParamsOrdered(
                "ctoken", continuationData.getContinuation(),
                "continuation", continuationData.getContinuation(),
                "itct", continuationData.getClickTrackingParams()
        );
    }

    URI newCommentApiRequestUri(NextContinuationData continuationData) {
        return URI.create(String.format(COMMENT_API_URI_TEMPLATE, buildQueryParams(continuationData)));
    }

    URI newBrowseApiRequestUri(NextContinuationData continuationData) {
        return URI.create(String.format(BROWSE_API_URI_TEMPLATE, buildBrowseQueryParams(continuationData)));
    }

    String newVideoPageUri(String videoId) {
        return String.format(VIDEO_PAGE_URI_TEMPLATE, videoId);
    }

    String newChanelPageUri(String chanelId) {
        return String.format(CHANNEL_PAGE_URI_TEMPLATE, chanelId);
    }

    String newChannelVideosPageUri(String channelVanityName) {
        return String.format(CHANNEL_VIDEOS_PAGE_URI_TEMPLATE, channelVanityName);
    }
}
