package com.ctzn.youtubescraper.examples.filesystem;

import com.ctzn.youtubescraper.core.config.CommentOrderCfg;

import static com.ctzn.youtubescraper.addons.customhandlers.CustomCommentRunnerFactory.newDefaultFileAppender;

public class CommentsNewestFirstExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) {
        String videoId = "v9ejT8FO-7I";
        Runnable runner = newDefaultFileAppender(videoId, CommentOrderCfg.NEWEST_FIRST);
        runner.run();
    }
}
