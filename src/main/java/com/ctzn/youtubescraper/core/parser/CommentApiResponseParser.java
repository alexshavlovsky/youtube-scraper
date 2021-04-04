package com.ctzn.youtubescraper.core.parser;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.model.comments.ApiResponse;

import static com.ctzn.youtubescraper.core.parser.json.JsonMapper.parse;

public class CommentApiResponseParser {

    private static final String RESPONSE_ARRAY_ENTRY_REGEX = "\"xsrf_token\"\\s*:\\s*\"";

    public <T extends ApiResponse> T parseResponseBody(String responseBody, Class<T> valueType) throws ScraperParserException {
        return parse(ParserUtil.parseEnclosingObjectByEntryRegex(RESPONSE_ARRAY_ENTRY_REGEX, responseBody), valueType);
    }
}
