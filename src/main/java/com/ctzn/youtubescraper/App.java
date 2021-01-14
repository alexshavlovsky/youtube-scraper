package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.formatter.CommentFormatter;
import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.FileAppenderHandler;
import com.ctzn.youtubescraper.http.YoutubeHttpClient;

public class App {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) throws Exception {
        String videoId = args[0];
        String fileName = videoId + "_" + System.currentTimeMillis() + ".txt";
        CommentFormatter formatter = new CommentHumanReadableFormatter();
        FileAppenderHandler fileHandler = new FileAppenderHandler(fileName, formatter);
//        ConsolePrinterHandler consoleHandler = new ConsolePrinterHandler(formatter);
        YoutubeHttpClient client = new YoutubeHttpClient(videoId, fileHandler);
        while (client.hasContinuation()) client.nextContinuation();
    }
}
