package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.model.ApiResponse;

import static com.ctzn.youtubescraper.parser.ModelMapper.parse;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseDigitsToInt;

public class CommentApiResponseParser {
    public <T extends ApiResponse> T parseResponseBody(String responseBody, Class<T> valueType) throws Exception {
        return parse(responseBody, valueType);
    }

    public int parseCommentsCountText(String text) {
        return parseDigitsToInt(text);
    }
}
