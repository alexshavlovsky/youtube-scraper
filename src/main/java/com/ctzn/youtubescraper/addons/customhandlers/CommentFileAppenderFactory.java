package com.ctzn.youtubescraper.addons.customhandlers;

import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
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

    public DataHandler<CommentDTO> newInstance(String videoId) {
        return new CommentFileAppender(
                RESULT_FOLDER + videoId + "_" + System.currentTimeMillis() + ".txt",
                new CommentHumanReadableFormatter()
        );
    }

    private static class InstanceHolder {
        static CommentFileAppenderFactory defaultInstance = new CommentFileAppenderFactory(".output");
    }

    public static CommentFileAppenderFactory getDefaultInstance() {
        return CommentFileAppenderFactory.InstanceHolder.defaultInstance;
    }

}
