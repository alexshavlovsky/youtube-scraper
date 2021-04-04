package com.ctzn.youtubescraper.addons.customhandlers;

import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;

import java.util.List;

public class CommentConsolePrinter implements DataHandler<CommentDTO> {

    private final CommentFormatter commentFormatter;

    public CommentConsolePrinter(CommentFormatter commentFormatter) {
        this.commentFormatter = commentFormatter;
    }

    @Override
    public void accept(List<CommentDTO> comments) {
        commentFormatter.printAll(comments);
    }

}
