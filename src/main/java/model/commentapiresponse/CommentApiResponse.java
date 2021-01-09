package model.commentapiresponse;

import lombok.Data;
import model.commentitemsection.CommentItemSection;

@Data
public class CommentApiResponse {
    public Response response;
    public String xsrf_token;

    public CommentItemSection getCommentItemSection() {
        return response.continuationContents.itemSectionContinuation;
    }
}
