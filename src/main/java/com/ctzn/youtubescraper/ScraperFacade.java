package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.FileAppenderHandler;
import com.ctzn.youtubescraper.http.YoutubeHttpClient;

public class ScraperFacade implements Runnable {
    private final String videoId;
    private final FileAppenderHandler fileHandler;

    public ScraperFacade(String videoId) {
        this.videoId = videoId;
        String outputFileName = videoId + "_" + System.currentTimeMillis() + ".txt";
        fileHandler = new FileAppenderHandler(outputFileName, new CommentHumanReadableFormatter());
    }

    @Override
    public void run() {
        YoutubeHttpClient client = null;
        try {
            client = new YoutubeHttpClient(videoId, fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        while (client.hasContinuation()) client.nextContinuation();
    }
}
