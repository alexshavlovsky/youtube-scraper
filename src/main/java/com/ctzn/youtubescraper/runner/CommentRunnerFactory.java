package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.iterator.comment.CommentVisitor;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContextBuilder;
import com.ctzn.youtubescraper.model.comments.CommentDTO;

import static com.ctzn.youtubescraper.iterator.comment.CommentVisitor.NO_LIMIT;

public class CommentRunnerFactory {

    public static Runnable newDefaultFileAppender(String videoId, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, DefaultCommentFileAppenderFactory.getInstance().newInstance(videoId), sortNewestCommentsFirst, NO_LIMIT, NO_LIMIT);
    }

    public static Runnable newFileAppender(String videoId, CommentFileAppenderFactory commentFileAppenderFactory, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, commentFileAppenderFactory.newInstance(videoId), sortNewestCommentsFirst, NO_LIMIT, NO_LIMIT);
    }

    public static Runnable newConsolePrinter(String videoId, boolean sortNewestCommentsFirst) {
        return newInstance(videoId, new CommentConsolePrinter(new CommentHumanReadableFormatter()), sortNewestCommentsFirst, NO_LIMIT, NO_LIMIT);
    }

    public static Runnable newInstance(String videoId, DataHandler<CommentDTO> handler, boolean sortNewestCommentsFirst, int totalCommentCountLimit, int replyThreadCountLimit) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, sortNewestCommentsFirst),
                new CommentVisitor(handler, totalCommentCountLimit, replyThreadCountLimit)
        );
    }

    public static Runnable newUnrestrictedInstance(String videoId, DataHandler<CommentDTO> handler) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, true),
                new CommentVisitor(handler, NO_LIMIT, NO_LIMIT)
        );
    }
}
