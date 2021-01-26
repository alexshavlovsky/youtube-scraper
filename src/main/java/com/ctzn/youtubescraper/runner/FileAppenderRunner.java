package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentFileAppender;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Log
abstract class FileAppenderRunner extends AbstractRunner {

    private final static String RESULT_FOLDER = ".output" + File.separator;

    static {
        try {
            Files.createDirectories(Paths.get(RESULT_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    FileAppenderRunner(String videoId) {
        super(videoId, List.of(
                new CommentFileAppender(
                        RESULT_FOLDER + videoId + "_" + System.currentTimeMillis() + ".txt",
                        new CommentHumanReadableFormatter()
                )
        ));
    }
}
