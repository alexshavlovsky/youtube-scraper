package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import lombok.Value;

@Value
public class CommentIteratorSettings {
    DataHandler<CommentDTO> handler;
    int commentCountLimit;
    int replyThreadCountLimit;
}
