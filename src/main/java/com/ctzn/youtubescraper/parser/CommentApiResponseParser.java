package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.model.commentapiresponse.CommentApiResponse;

import static com.ctzn.youtubescraper.parser.ModelMapper.parse;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseDigitsToInt;

public class CommentApiResponseParser {
    public CommentApiResponse parseResponseBody(String responseBody) {
        return parse(responseBody, CommentApiResponse.class);
    }

    public int parseCommentsCountText(String text) {
        return parseDigitsToInt(text);
    }
}
