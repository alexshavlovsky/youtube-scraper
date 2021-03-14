package com.ctzn.youtubescraper.model.comments;

public class CommentApiResponse implements ApiResponse {
    public Response response;
    public String xsrf_token;

    @Override
    public CommentItemSection getItemSection() {
        return response.continuationContents.itemSectionContinuation;
    }

    @Override
    public String getToken() {
        return xsrf_token;
    }

    static class ContinuationContents {
        public CommentItemSection itemSectionContinuation;
    }

    static class Response {
        public ContinuationContents continuationContents;
//        public String trackingParams;
    }
}
