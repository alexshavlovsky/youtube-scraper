package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.iterator.comment.CommentIteratorSettings;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContextBuilder;
import com.ctzn.youtubescraper.model.comments.CommentDTO;

public class CommentRunnerFactory {

    public static Runnable newDefaultFileAppender(String videoId, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, DefaultCommentFileAppenderFactory.getInstance().newInstance(videoId), sortNewestCommentsFirst, 0, 0);
    }

    public static Runnable newFileAppender(String videoId, CommentFileAppenderFactory commentFileAppenderFactory, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, commentFileAppenderFactory.newInstance(videoId), sortNewestCommentsFirst, 0, 0);
    }

    public static Runnable newConsolePrinter(String videoId, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, new CommentConsolePrinter(new CommentHumanReadableFormatter()), sortNewestCommentsFirst, 0, 0);
    }

    public static Runnable newInstance(String videoId, DataHandler<CommentDTO> handler, boolean sortNewestCommentsFirst, int totalCommentCountLimit, int replyThreadCountLimit) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, sortNewestCommentsFirst),
                new CommentIteratorSettings(handler, totalCommentCountLimit, replyThreadCountLimit)
        );
    }

    public static Runnable newUnrestrictedInstance(String videoId, DataHandler<CommentDTO> handler) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, true),
                new CommentIteratorSettings(handler, 0, 0)
        );
    }
}
