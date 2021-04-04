package com.ctzn.youtubescraper.examples.filesystem;

import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.executor.CustomExecutorService;

import java.util.Arrays;

import static com.ctzn.youtubescraper.addons.customhandlers.CustomCommentRunnerFactory.newDefaultFileAppender;

public class CommentsConcurrentExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    private static String[] ids = {
            "D2bB1bz9Z9s", "LqihfRVj8hM", "_oaSgmoy9aA", "lIlSNpLkO-A", "XQ_cQ9I7_YA",
            "Dtk2xgBZTec", "pEr1TtCB7_Y", "NMg6DQSO5VE", "bhE2RaN4VcI", "pJJE7R8xteQ"
    };

    public static void main(String[] args) throws InterruptedException {
        CustomExecutorService executor = CustomExecutorService.newInstance();

        Arrays.stream(ids).map(videoId -> newDefaultFileAppender(videoId, CommentOrderCfg.NEWEST_FIRST)).forEach(executor::submit);
        executor.awaitAndTerminate();
    }

}
