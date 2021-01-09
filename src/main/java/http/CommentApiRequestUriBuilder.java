package http;

import model.ContinuationData;

import static http.IoUtil.joinQueryParamsOrdered;

class CommentApiRequestUriBuilder {

    private final static String COMMENT_API_URI_TEMPLATE = "https://www.youtube.com/comment_service_ajax?%s";

    private static String buildQueryParams(ContinuationData continuationData) {
        return joinQueryParamsOrdered(
                "action_get_comments", "1",
                "pbj", "1",
                "ctoken", continuationData.continuation,
                "continuation", continuationData.continuation,
                "itct", continuationData.clickTrackingParams
        );
    }

    static String buildCommentApiRequestUri(ContinuationData continuationData) {
        return String.format(COMMENT_API_URI_TEMPLATE, buildQueryParams(continuationData));
    }
}
