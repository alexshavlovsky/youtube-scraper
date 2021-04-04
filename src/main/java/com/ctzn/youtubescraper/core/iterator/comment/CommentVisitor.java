package com.ctzn.youtubescraper.core.iterator.comment;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
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
