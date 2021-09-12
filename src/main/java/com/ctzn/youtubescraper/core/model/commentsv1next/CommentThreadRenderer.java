package com.ctzn.youtubescraper.core.model.commentsv1next;

public class CommentThreadRenderer {
    public Comment comment;
    public Replies replies;
    public String trackingParams;
    public String renderingPriority;
    public boolean isModeratedElqComment;

    public static CommentThreadRenderer fromCommentRenderer(CommentRenderer commentRenderer) {
        CommentThreadRenderer res = new CommentThreadRenderer();
        res.comment = Comment.fromCommentRenderer(commentRenderer);
        return res;
    }
}
