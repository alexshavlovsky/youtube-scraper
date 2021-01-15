package com.ctzn.youtubescraper;

public class App {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) throws Exception {
//        ScraperFacade concurrent = new ScraperFacade("Pgc0ZYDzazY");
//        Thread runner = new Thread(concurrent);
//        runner.start();

        String videoId = args[0];
        ScraperFacade scraper = new ScraperFacade(videoId);
        scraper.run();
    }
}
