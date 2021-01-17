package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.FileAppenderHandler;
import com.ctzn.youtubescraper.iterator.CommentContextIterator;
import com.ctzn.youtubescraper.iterator.IterableCommentContext;
import com.ctzn.youtubescraper.iterator.IterableCommentContextFactory;
import lombok.extern.java.Log;

import java.util.List;

@Log
public class NewestFirstHumanReadableFileAppenderRunner implements Runnable {
    private final String videoId;
    private final FileAppenderHandler fileHandler;

    public NewestFirstHumanReadableFileAppenderRunner(String videoId) {
        this.videoId = videoId;
        String outputFileName = videoId + "_" + System.currentTimeMillis() + ".txt";
        fileHandler = new FileAppenderHandler(outputFileName, new CommentHumanReadableFormatter());
    }

    @Override
    public void run() {
        try {
            IterableCommentContext commentContext = IterableCommentContextFactory.newNewestFirstContext(videoId);
            CommentContextIterator iterator = new CommentContextIterator(commentContext, List.of(fileHandler));
            iterator.traverse();
            log.info(videoId + " DONE");
        } catch (Exception e) {
            log.warning(videoId + " FAILED: " + e.toString());
        }
    }
}
