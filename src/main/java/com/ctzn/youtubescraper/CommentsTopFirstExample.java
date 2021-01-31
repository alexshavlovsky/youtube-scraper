package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

public class CommentsTopFirstExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) {
        String videoId = "v9ejT8FO-7I";
        Runnable runner = CommentRunnerFactory.newDefaultFileAppender(videoId, false);
        runner.run();
    }
}
