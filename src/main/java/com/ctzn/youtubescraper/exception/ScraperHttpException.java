package com.ctzn.youtubescraper.exception;

public class ScraperHttpException extends ScraperException {

    public ScraperHttpException(String message) {
        super(message);
    }

    public ScraperHttpException(String format, Object... args) {
        super(format, args);
    }

    public ScraperHttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
