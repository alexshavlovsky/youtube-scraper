package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.IterableCommentContextFactory;

public class RunnerFactory {

    public static Runnable newNewestCommentsFirstFileAppenderRunner(String videoId) {
        return new FileAppenderRunner(videoId) {
            @Override
            IterableCommentContext newCommentContext(String videoId) throws ScraperException {
                return IterableCommentContextFactory.newNewestCommentsFirstContext(videoId);
            }
        };
    }

    public static Runnable newTopCommentsFirstFileAppenderRunner(String videoId) {
        return new FileAppenderRunner(videoId) {
            @Override
            IterableCommentContext newCommentContext(String videoId) throws ScraperException {
                return IterableCommentContextFactory.newTopCommentsFirstContext(videoId);
            }
        };
    }

    public static Runnable newNewestCommentsFirstConsolePrinterRunner(String videoId) {
        return new ConsolePrinterRunner(videoId) {
            @Override
            IterableCommentContext newCommentContext(String videoId) throws ScraperException {
                return IterableCommentContextFactory.newNewestCommentsFirstContext(videoId);
            }
        };
    }

    public static Runnable newTopCommentsFirstConsolePrinterRunner(String videoId) {
        return new ConsolePrinterRunner(videoId) {
            @Override
            IterableCommentContext newCommentContext(String videoId) throws ScraperException {
                return IterableCommentContextFactory.newTopCommentsFirstContext(videoId);
            }
        };
    }
}
