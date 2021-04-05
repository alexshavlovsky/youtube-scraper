package com.ctzn.youtubescraper.core.persistence;


import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.iterator.comment.CommentVisitor;
import com.ctzn.youtubescraper.core.iterator.comment.IterableCommentContextBuilder;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;

public class CommentRunnerFactory {

    public static CommentRunner newInstance(String videoId, DataHandler<CommentDTO> handler, CommentOrderCfg commentOrderCfg, CommentIteratorCfg commentIteratorCfg) {
        return new CommentRunner(
                new IterableCommentContextBuilder(videoId, commentOrderCfg),
                new CommentVisitor(handler, commentIteratorCfg)
        );
    }

}
