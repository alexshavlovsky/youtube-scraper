package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.config.CommentOrderCfg;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

public class CommentsNewestFirstExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) {
        String videoId = "v9ejT8FO-7I";
        Runnable runner = CommentRunnerFactory.newDefaultFileAppender(videoId, CommentOrderCfg.NEWEST_FIRST);
        runner.run();
    }
}
