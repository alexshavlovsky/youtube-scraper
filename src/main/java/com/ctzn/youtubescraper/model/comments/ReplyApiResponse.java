package com.ctzn.youtubescraper.model.comments;

import com.ctzn.youtubescraper.parser.json.JsonUnwrapProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplyApiResponse implements ApiResponse {
    public Response response;
    public String xsrf_token;

    @Override
    public CommentItemSection getItemSection() {
        return response.itemSection;
    }

    @Override
    public String getToken() {
        return xsrf_token;
    }

    static class Response {
        @JsonProperty("continuationContents")
        @JsonUnwrapProperty("commentRepliesContinuation")
        CommentItemSection itemSection;
    }
}
