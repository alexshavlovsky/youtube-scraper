package com.ctzn.youtubescraper.config;

import lombok.Data;

@Data
public class CommentIteratorCfg {

    public static final int NO_LIMIT = -1;

    public int commentCountPerVideoLimit = NO_LIMIT;
    public int replyThreadCountLimit = NO_LIMIT;


    private CommentIteratorCfg() {
    }

    private CommentIteratorCfg(int commentCountPerVideoLimit, int replyThreadCountLimit) {
        this.commentCountPerVideoLimit = commentCountPerVideoLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
    }

    public static CommentIteratorCfg newInstance() {
        return new CommentIteratorCfg();
    }

    public static CommentIteratorCfg newInstance(int commentCountPerVideoLimit, int replyThreadCountLimit) {
        return new CommentIteratorCfg(commentCountPerVideoLimit, replyThreadCountLimit);
    }

}
