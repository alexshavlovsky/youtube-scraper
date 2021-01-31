package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.iterator.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.IterableCommentContextFactory;

import java.util.List;

public class CommentRunnerFactory {

    public static Runnable newNewestCommentsFirstFileAppenderRunner(String videoId) {
        return new FileAppenderRunner(videoId) {
            @Override
            IterableCommentContext newCommentContext(String videoId) throws ScraperException {
                return IterableCommentContextFactory.newNewestCommentsFirstContext(videoId);
            }

            @Override
            CommentContextIterator newCommentContextIterator(IterableCommentContext commentContext, List<CommentHandler> handlers) {
                return new CommentContextIterator(commentContext, handlers);
            }
        };
    }

    public static Runnable newTopCommentsFirstFileAppenderRunner(String videoId) {
        return new FileAppenderRunner(videoId) {
            @Override
            IterableCommentContext newCommentContext(String videoId) throws ScraperException {
                return IterableCommentContextFactory.newTopCommentsFirstContext(videoId);
            }

            @Override
            CommentContextIterator newCommentContextIterator(IterableCommentContext commentContext, List<CommentHandler> handlers) {
                return new CommentContextIterator(commentContext, handlers);
            }
        };
    }

    public static Runnable newNewestCommentsFirstConsolePrinterRunner(String videoId) {
        return new CustomCommentRunner(videoId, List.of(new CommentConsolePrinter(new CommentHumanReadableFormatter())), true);
    }

    public static Runnable newTopCommentsFirstConsolePrinterRunner(String videoId) {
        return new CustomCommentRunner(videoId, List.of(new CommentConsolePrinter(new CommentHumanReadableFormatter())), false);
    }
}
