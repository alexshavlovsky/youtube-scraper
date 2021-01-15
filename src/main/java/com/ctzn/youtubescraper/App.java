package com.ctzn.youtubescraper;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    private static String[] ids = {
            "D2bB1bz9Z9s", "LqihfRVj8hM", "_oaSgmoy9aA", "lIlSNpLkO-A", "XQ_cQ9I7_YA",
            "Dtk2xgBZTec", "pEr1TtCB7_Y", "NMg6DQSO5VE", "bhE2RaN4VcI", "pJJE7R8xteQ"
    };

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Arrays.stream(ids).map(ScraperFacade::new).forEach(executor::submit);
        executor.shutdown();

//        String videoId = args[0];
//        ScraperFacade scraper = new ScraperFacade(videoId);
//        scraper.run();
    }
}
