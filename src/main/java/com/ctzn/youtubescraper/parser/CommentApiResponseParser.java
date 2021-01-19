package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.ApiResponse;

import static com.ctzn.youtubescraper.parser.ModelMapper.parse;

public class CommentApiResponseParser {
    public <T extends ApiResponse> T parseResponseBody(String responseBody, Class<T> valueType) throws ScraperParserException {
        return parse(responseBody, valueType);
    }
}
