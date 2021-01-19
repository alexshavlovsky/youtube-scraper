package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

class ModelMapper {

    private static ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @SuppressWarnings("unchecked")
    static Map<String, Object> parseJsSoup(String jsonSoup) throws ScraperParserException {
        try {
            return objectMapper.readValue(jsonSoup, Map.class);
        } catch (JsonProcessingException e) {
            throw new ScraperParserException(e.getMessage(), e);
        }
    }

    static <T> T parse(String json, Class<T> valueType) throws ScraperParserException {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new ScraperParserException(e.getMessage(), e);
        }
    }
}
