package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.formatter.CommentFormatter;
import com.ctzn.youtubescraper.model.CommentDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileAppenderHandler implements CommentHandler {
    private final String file;
    private final CommentFormatter commentFormatter;

    public FileAppenderHandler(String file, CommentFormatter commentFormatter) {
        this.file = file;
        this.commentFormatter = commentFormatter;

        Path fileToDeletePath = Paths.get(file);
        try {
            Files.delete(fileToDeletePath);
        } catch (NoSuchFileException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(List<CommentDTO> comments) {
        commentFormatter.appendAll(file, comments);
    }
}
