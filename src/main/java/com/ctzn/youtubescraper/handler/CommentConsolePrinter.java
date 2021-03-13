package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.commentformatter.CommentFormatter;
import com.ctzn.youtubescraper.model.comments.CommentDTO;

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
