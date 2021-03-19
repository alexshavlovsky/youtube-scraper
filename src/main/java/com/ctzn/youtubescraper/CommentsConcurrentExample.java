package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.executor.CustomExecutorService;
import com.ctzn.youtubescraper.config.CommentOrderCfg;

import java.util.Arrays;

import static com.ctzn.youtubescraper.runner.CommentRunnerFactory.newDefaultFileAppender;

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

        Arrays.stream(ids).map(videoId -> newDefaultFileAppender(videoId, CommentOrderCfg.newestFirst())).forEach(executor::submit);
        executor.awaitAndTerminate();
    }

}
