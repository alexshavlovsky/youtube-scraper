package com.ctzn.youtubescraper.exception;

public class ScraperException extends Exception {
    public ScraperException(String message) {
        super(message);
    }

    public ScraperException(String format, Object... args) {
        super(String.format(format, args));
    }

    public ScraperException(String message, Throwable cause) {
        super(message, cause);
    }
}
