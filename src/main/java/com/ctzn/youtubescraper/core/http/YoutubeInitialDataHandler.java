package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;

@FunctionalInterface
interface YoutubeInitialDataHandler<E> {
    E handle(String ytInitialDataJson) throws ScraperParserException;
}
