package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.runner.RunnerFactory;

public class CommentsTopFirstExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) {
        String videoId = "v9ejT8FO-7I";
        Runnable runner = RunnerFactory.newTopCommentsFirstFileAppenderRunner(videoId);
        runner.run();
    }
}
