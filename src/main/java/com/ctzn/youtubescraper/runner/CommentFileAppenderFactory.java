package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentFileAppender;
import com.ctzn.youtubescraper.handler.CommentHandler;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log
public class CommentFileAppenderFactory {

    private final String RESULT_FOLDER;

    public CommentFileAppenderFactory(String folder) {
        RESULT_FOLDER = folder + File.separator;
        createFolder(RESULT_FOLDER);
    }

    private void createFolder(String folder) {
        try {
            Files.createDirectories(Paths.get(folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommentHandler newInstance(String videoId) {
        return new CommentFileAppender(
                RESULT_FOLDER + videoId + "_" + System.currentTimeMillis() + ".txt",
                new CommentHumanReadableFormatter()
        );
    }
}
