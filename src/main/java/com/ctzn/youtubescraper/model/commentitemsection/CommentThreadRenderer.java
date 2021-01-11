package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class CommentThreadRenderer {
    public Comment comment;
    public Replies replies;
    public CommentTargetTitle commentTargetTitle;
    public String trackingParams;
    public String renderingPriority;
    public LoggingDirectives loggingDirectives;
}
