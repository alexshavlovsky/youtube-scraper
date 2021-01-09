package model.commentitemsection;

import lombok.Data;

@Data
class CommentThreadRenderer {
    public Comment comment;
    public Replies replies;
    public CommentTargetTitle commentTargetTitle;
    public String trackingParams;
    public String renderingPriority;
    public LoggingDirectives loggingDirectives;
}
