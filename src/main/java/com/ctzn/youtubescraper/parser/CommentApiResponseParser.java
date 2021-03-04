package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.comments.ApiResponse;

import static com.ctzn.youtubescraper.parser.json.JsonMapper.parse;

public class CommentApiResponseParser {

    private static final String RESPONSE_ARRAY_ENTRY_REGEX = "\"xsrf_token\"\\s*:\\s*\"";

    public <T extends ApiResponse> T parseResponseBody(String responseBody, Class<T> valueType) throws ScraperParserException {
        return parse(ParserUtil.parseEnclosingObjectByEntryRegex(RESPONSE_ARRAY_ENTRY_REGEX, responseBody), valueType);
    }
}
