package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.model.CommentDTO;

import java.util.List;

public interface CommentHandler {
    void handle(List<CommentDTO> comments);
}
