package parser;

import model.commentapiresponse.CommentApiResponse;

import static parser.ModelMapper.parse;

public class CommentApiResponseParser {
    public static CommentApiResponse parseResponseBody(String responseBody) {
        return parse(responseBody, CommentApiResponse.class);
    }
}
