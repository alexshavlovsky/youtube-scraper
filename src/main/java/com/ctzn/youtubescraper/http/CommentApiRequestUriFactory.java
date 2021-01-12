package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.model.continuation.NextContinuationData;

import java.net.URI;

import static com.ctzn.youtubescraper.http.IoUtil.joinQueryParamsOrdered;

class CommentApiRequestUriFactory {

    private final static String COMMENT_API_URI_TEMPLATE = "https://www.youtube.com/comment_service_ajax?%s";

    private static String buildQueryParams(NextContinuationData continuationData) {
        return joinQueryParamsOrdered(
                "action_get_comments", "1",
                "pbj", "1",
                "ctoken", continuationData.continuation,
                "continuation", continuationData.continuation,
                "itct", continuationData.clickTrackingParams
        );
    }

    URI newRequestUri(NextContinuationData continuationData) {
        return URI.create(String.format(COMMENT_API_URI_TEMPLATE, buildQueryParams(continuationData)));
    }
}
