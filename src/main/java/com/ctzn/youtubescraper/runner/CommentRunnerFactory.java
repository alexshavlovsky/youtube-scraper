package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.iterator.comment.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContextBuilder;
import com.ctzn.youtubescraper.model.comments.CommentDTO;

import java.util.List;

public class CommentRunnerFactory {

    public static Runnable newDefaultFileAppender(String videoId, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, List.of(DefaultCommentFileAppenderFactory.getInstance().newInstance(videoId)), sortNewestCommentsFirst, 0, 0);
    }

    public static Runnable newFileAppender(String videoId, CommentFileAppenderFactory commentFileAppenderFactory, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, List.of(commentFileAppenderFactory.newInstance(videoId)), sortNewestCommentsFirst, 0, 0);
    }

    public static Runnable newConsolePrinter(String videoId, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, List.of(new CommentConsolePrinter(new CommentHumanReadableFormatter())), sortNewestCommentsFirst, 0, 0);
    }

    public static Runnable newInstance(String videoId, List<DataHandler<CommentDTO>> handlers, boolean sortNewestCommentsFirst, int totalCommentCountLimit, int replyThreadCountLimit) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, sortNewestCommentsFirst),
                new CommentContextIterator(handlers, totalCommentCountLimit, replyThreadCountLimit)
        );
    }

    public static Runnable newUnrestrictedInstance(String videoId, List<DataHandler<CommentDTO>> handlers) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, true),
                new CommentContextIterator(handlers, 0, 0)
        );
    }
}
