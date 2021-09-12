package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.model.YoutubeCfgDTO;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;

import java.net.URI;

import static com.ctzn.youtubescraper.core.http.IoUtil.joinQueryParamsOrdered;

class YoutubeUriFactory {

    private final static String CHANNEL_PAGE_URI_TEMPLATE = "https://www.youtube.com/channel/%s";
    private final static String CHANNEL_VIDEOS_PAGE_URI_TEMPLATE = "https://www.youtube.com/%s/videos";
    private final static String VIDEO_PAGE_URI_TEMPLATE = "https://www.youtube.com/watch?v=%s";
    private final static String COMMENT_API_URI_TEMPLATE = "https://www.youtube.com/comment_service_ajax?%s";
    private final static String BROWSE_API_URI_TEMPLATE = "https://www.youtube.com/browse_ajax?%s";
    private final static String BROWSE_API_V1_URI_TEMPLATE = "https://www.youtube.com/youtubei/v1/browse?%s";
    private final static String NEXT_API_V1_URI_TEMPLATE = "https://www.youtube.com/youtubei/v1/next?%s";

    private static String buildCommentApiQueryParams(NextContinuationData continuationData) {
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

    private static String buildReplyApiQueryParams(NextContinuationData continuationData) {
        return joinQueryParamsOrdered(
                "action_get_comment_replies", "1",
                "pbj", "1",
                "ctoken", continuationData.getContinuation(),
                "continuation", continuationData.getContinuation(),
                "type", "next",
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

    private static String buildBrowseV1QueryParams(YoutubeCfgDTO youtubeCfgDTO) {
        return joinQueryParamsOrdered("key", youtubeCfgDTO.getApiKey());
    }

    URI newCommentApiRequestUri(NextContinuationData continuationData) {
        return URI.create(String.format(COMMENT_API_URI_TEMPLATE, buildCommentApiQueryParams(continuationData)));
    }

    URI newReplyApiRequestUri(NextContinuationData continuationData) {
        return URI.create(String.format(COMMENT_API_URI_TEMPLATE,  buildReplyApiQueryParams(continuationData) ));
    }

    URI newBrowseApiRequestUri(NextContinuationData continuationData) {
        return URI.create(String.format(BROWSE_API_URI_TEMPLATE, buildBrowseQueryParams(continuationData)));
    }

    URI newBrowseApiV1RequestUri(YoutubeCfgDTO youtubeCfgDTO) {
        return URI.create(String.format(BROWSE_API_V1_URI_TEMPLATE, buildBrowseV1QueryParams(youtubeCfgDTO)));
    }

    URI newNextApiV1RequestUri(YoutubeCfgDTO youtubeCfgDTO) {
        return URI.create(String.format(NEXT_API_V1_URI_TEMPLATE, buildBrowseV1QueryParams(youtubeCfgDTO)));
    }

    String newVideoPageUri(String videoId) {
        return String.format(VIDEO_PAGE_URI_TEMPLATE, videoId);
    }

    String newChanelPageUri(String channelId) {
        return String.format(CHANNEL_PAGE_URI_TEMPLATE, channelId);
    }

    String newChannelVideosPageUri(String channelVanityName) {
        return String.format(CHANNEL_VIDEOS_PAGE_URI_TEMPLATE, channelVanityName);
    }
}
