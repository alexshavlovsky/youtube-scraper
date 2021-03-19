package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.iterator.comment.CommentVisitor;
import com.ctzn.youtubescraper.iterator.comment.IterableCommentContextBuilder;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.config.CommentOrderCfg;

public class CommentRunnerFactory {

    public static Runnable newDefaultFileAppender(String videoId, CommentOrderCfg commentOrderCfg) {
        return newInstance(videoId, DefaultCommentFileAppenderFactory.getInstance().newInstance(videoId), commentOrderCfg, CommentIteratorCfg.newInstance());
    }

    public static Runnable newFileAppender(String videoId, CommentFileAppenderFactory commentFileAppenderFactory, CommentOrderCfg commentOrderCfg) {
        return newInstance(videoId, commentFileAppenderFactory.newInstance(videoId), commentOrderCfg, CommentIteratorCfg.newInstance());
    }

    public static Runnable newConsolePrinter(String videoId, CommentOrderCfg commentOrderCfg) {
        return newInstance(videoId, new CommentConsolePrinter(new CommentHumanReadableFormatter()), commentOrderCfg, CommentIteratorCfg.newInstance());
    }

    public static Runnable newInstance(String videoId, DataHandler<CommentDTO> handler, CommentOrderCfg commentOrderCfg, CommentIteratorCfg commentIteratorCfg) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, commentOrderCfg),
                new CommentVisitor(handler, commentIteratorCfg)
        );
    }

    public static Runnable newUnrestrictedInstance(String videoId, DataHandler<CommentDTO> handler) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, CommentOrderCfg.newestFirst()),
                new CommentVisitor(handler, CommentIteratorCfg.newInstance())
        );
    }

}
