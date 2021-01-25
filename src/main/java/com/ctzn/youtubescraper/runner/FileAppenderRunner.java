package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentFileAppender;
import lombok.extern.java.Log;

import java.util.List;

@Log
abstract class FileAppenderRunner extends AbstractRunner {

    FileAppenderRunner(String videoId) {
        super(videoId, List.of(
                new CommentFileAppender(
                        videoId + "_" + System.currentTimeMillis() + ".txt",
                        new CommentHumanReadableFormatter()
                )
        ));
    }
}
