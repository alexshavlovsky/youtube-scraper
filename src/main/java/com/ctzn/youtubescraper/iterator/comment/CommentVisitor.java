package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.config.CommentIteratorCfg;
import lombok.Value;

import java.util.List;

@Value
public class CommentVisitor {

    DataHandler<CommentDTO> handler;
    CommentIteratorCfg commentIteratorCfg;

    void visitAll(List<CommentDTO> list) {
        handler.accept(list);
    }

    void visit(CommentDTO comment) {
        visitAll(List.of(comment));
    }

}
