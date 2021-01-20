package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.runner.RunnerFactory;

public class CommentsNewestFirstExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) {
        String videoId = "v9ejT8FO-7I";
        Runnable runner = RunnerFactory.newNewestCommentsFirstFileAppenderRunner(videoId);
        runner.run();
    }
}
