package com.ctzn.youtubescraper.addons.customhandlers;

import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CommentFileAppender implements DataHandler<CommentDTO> {

    private final String file;
    private final CommentFormatter commentFormatter;

    public CommentFileAppender(String file, CommentFormatter commentFormatter) {
        this.file = file;
        this.commentFormatter = commentFormatter;
        deleteIfExists(file);
    }

    private static void deleteIfExists(String file) {
        Path fileToDeletePath = Paths.get(file);
        try {
            Files.delete(fileToDeletePath);
        } catch (NoSuchFileException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(List<CommentDTO> comments) {
        commentFormatter.appendAll(file, comments);
    }

}
