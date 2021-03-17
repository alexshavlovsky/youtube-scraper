package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import lombok.Value;

import java.util.List;

@Value
public class CommentVisitor {

    public static final int NO_LIMIT = -1;

    DataHandler<CommentDTO> handler;
    int commentCountLimit;
    int replyThreadCountLimit;

    void visitAll(List<CommentDTO> list) {
        handler.accept(list);
    }

    void visit(CommentDTO comment) {
        visitAll(List.of(comment));
    }

}
