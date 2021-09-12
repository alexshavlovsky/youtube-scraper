package com.ctzn.youtubescraper.core.model.commentsv1next;

public class Content {
    public CommentThreadRenderer commentThreadRenderer;
    public CommentRenderer commentRenderer;
    public ContinuationItemRenderer continuationItemRenderer;

    public CommentRenderer getCommentRenderer() {
        return commentThreadRenderer != null ? commentThreadRenderer.comment.commentRenderer : commentRenderer;
    }
}
