package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.browsev1.BrowseV1Response;
import com.ctzn.youtubescraper.model.channelvideos.BrowseApiResponse;

import static com.ctzn.youtubescraper.parser.json.JsonMapper.parse;

public class BrowseApiResponseParser {

    private static final String RESPONSE_ARRAY_ENTRY_REGEX = "\"xsrf_token\"\\s*:\\s*\"";

    public BrowseApiResponse parseResponseBody(String responseBody) throws ScraperParserException {
        // To increase performance and reduce GC pressure it possible to skip entire deserialization
        // of a response object but instead deserialize only video grid continuation contents
        // Browse api response contains huge payload of auxiliary data and other junk
        return parse(ParserUtil.parseEnclosingObjectByEntryRegex(RESPONSE_ARRAY_ENTRY_REGEX, responseBody), BrowseApiResponse.class);
    }

    public BrowseV1Response parseResponseV1Body(String responseBody) throws ScraperParserException {
        return parse(responseBody, BrowseV1Response.class);
    }
}
