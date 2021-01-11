package com.ctzn.youtubescraper.model.commentapiresponse;

import lombok.Value;
import com.ctzn.youtubescraper.model.commentitemsection.CommentItemSection;

@Value
public class CommentApiResponse {
    public Response response;
    public String xsrf_token;

    public CommentItemSection getCommentItemSection() {
        return response.continuationContents.itemSectionContinuation;
    }
}
