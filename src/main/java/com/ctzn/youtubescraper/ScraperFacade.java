package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.FileAppenderHandler;
import com.ctzn.youtubescraper.http.YoutubeHttpClient;
import lombok.extern.java.Log;

@Log
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
        YoutubeHttpClient client;
        try {
            client = new YoutubeHttpClient(videoId, fileHandler);
        } catch (Exception e) {
            log.warning(videoId + " FAILED: " + e.getMessage());
            return;
        }
        while (client.hasContinuation()) client.nextContinuation();
        log.info(videoId + " DONE");
    }
}
