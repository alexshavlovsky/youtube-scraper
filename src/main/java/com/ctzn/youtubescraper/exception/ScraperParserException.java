package com.ctzn.youtubescraper.exception;

public class ScraperParserException extends ScraperException {

    public ScraperParserException(String message) {
        super(message);
    }

    public ScraperParserException(String format, Object... args) {
        super(format, args);
    }

    public ScraperParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
