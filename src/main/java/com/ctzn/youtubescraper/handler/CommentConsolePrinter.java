package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.commentformatter.CommentFormatter;
import com.ctzn.youtubescraper.model.CommentDTO;

import java.util.List;

public class CommentConsolePrinter implements CommentHandler {
    private final CommentFormatter commentFormatter;

    public CommentConsolePrinter(CommentFormatter commentFormatter) {
        this.commentFormatter = commentFormatter;
    }

    @Override
    public void handle(List<CommentDTO> comments) {
        commentFormatter.printAll(comments);
    }
}
