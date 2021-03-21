package com.ctzn.youtubescraper.config;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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

}
