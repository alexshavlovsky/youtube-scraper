package com.ctzn.youtubescraper.addons.customhandlers;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;

import static com.ctzn.youtubescraper.core.persistence.CommentRunnerFactory.newInstance;

public class CustomCommentRunnerFactory {

    public static Runnable newDefaultFileAppender(String videoId, CommentOrderCfg commentOrderCfg) {
        return newInstance(videoId, CommentFileAppenderFactory.getDefaultInstance().newInstance(videoId), commentOrderCfg, CommentIteratorCfg.newInstance());
    }

    public static Runnable newFileAppender(String videoId, CommentFileAppenderFactory commentFileAppenderFactory, CommentOrderCfg commentOrderCfg) {
        return newInstance(videoId, commentFileAppenderFactory.newInstance(videoId), commentOrderCfg, CommentIteratorCfg.newInstance());
    }

    public static Runnable newConsolePrinter(String videoId, CommentOrderCfg commentOrderCfg) {
        return newInstance(videoId, new CommentConsolePrinter(new CommentHumanReadableFormatter()), commentOrderCfg, CommentIteratorCfg.newInstance());
    }

}
