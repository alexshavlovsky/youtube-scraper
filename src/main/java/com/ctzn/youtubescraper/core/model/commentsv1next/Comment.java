package com.ctzn.youtubescraper.core.model.commentsv1next;

public class Comment {
    public CommentRenderer commentRenderer;

    public static Comment fromCommentRenderer(CommentRenderer commentRenderer) {
        Comment res = new Comment();
        res.commentRenderer = commentRenderer;
        return res;
    }
}
