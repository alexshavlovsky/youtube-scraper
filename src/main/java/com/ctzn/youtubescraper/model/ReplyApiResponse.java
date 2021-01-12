package com.ctzn.youtubescraper.model;

public class ReplyApiResponse implements ApiResponse {
    public Response response;
    public String xsrf_token;

    @Override
    public CommentItemSection getItemSection() {
        return response.continuationContents.commentRepliesContinuation;
    }

    @Override
    public String getToken() {
        return xsrf_token;
    }

    static class ContinuationContents {
        public CommentItemSection commentRepliesContinuation;
    }

    static class Response {
        public ContinuationContents continuationContents;
        public String trackingParams;
    }
}
