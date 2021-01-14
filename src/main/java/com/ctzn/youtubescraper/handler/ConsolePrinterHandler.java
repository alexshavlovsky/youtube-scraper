package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.formatter.CommentFormatter;
import com.ctzn.youtubescraper.model.CommentDTO;

import java.util.List;

public class ConsolePrinterHandler implements CommentHandler {
    private final CommentFormatter commentFormatter;

    public ConsolePrinterHandler(CommentFormatter commentFormatter) {
        this.commentFormatter = commentFormatter;
    }

    @Override
    public void handle(List<CommentDTO> comments) {
        commentFormatter.printAll(comments);
    }
}
