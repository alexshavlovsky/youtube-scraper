package com.ctzn.youtubescraper.core.exception;

public class ScrapperInterruptedException extends ScraperException {

    public ScrapperInterruptedException(String message) {
        super(message);
    }

    public ScrapperInterruptedException(String format, Object... args) {
        super(format, args);
    }

    public ScrapperInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
