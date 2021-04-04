package com.ctzn.youtubescraper.core.config;

import lombok.Getter;

@Getter
public class CommentIteratorCfg {

    private final CountLimit commentPerVideoLimit = new CountLimit();
    private final CountLimit replyPerCommentLimit = new CountLimit();

    private CommentIteratorCfg() {
    }

    private CommentIteratorCfg(int commentPerVideoLimit, int replyPerCommentLimit) {
        this.commentPerVideoLimit.set(commentPerVideoLimit);
        this.replyPerCommentLimit.set(replyPerCommentLimit);
    }

    public static CommentIteratorCfg newInstance() {
        return new CommentIteratorCfg();
    }

    public static CommentIteratorCfg newInstance(int commentCountPerVideoLimit, int replyThreadCountLimit) {
        return new CommentIteratorCfg(commentCountPerVideoLimit, replyThreadCountLimit);
    }

    @Override
    public String toString() {
        return "commentPerVideoLimit=" + commentPerVideoLimit +
                ", replyPerCommentLimit=" + replyPerCommentLimit;
    }

}
