package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.model.CommentDTO;

import java.util.ArrayList;
import java.util.List;

public class CommentCollector implements CommentHandler {

    private final List<CommentDTO> comments = new ArrayList<>();

    public CommentCollector() {
    }

    @Override
    public void handle(List<CommentDTO> items) {
        comments.addAll(items);
    }

    public List<CommentDTO> getComments() {
        return comments;
    }
}
