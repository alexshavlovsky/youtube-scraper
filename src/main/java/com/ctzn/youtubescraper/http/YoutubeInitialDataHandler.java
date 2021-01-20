package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperParserException;

@FunctionalInterface
interface YoutubeInitialDataHandler<E> {
    E handle(String ytInitialDataJson) throws ScraperParserException;
}
