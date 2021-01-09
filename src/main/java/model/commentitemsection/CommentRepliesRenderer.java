package model.commentitemsection;

import lombok.Data;

import java.util.List;

@Data
class CommentRepliesRenderer {
    public List<Continuation> continuations;
    public MoreText moreText;
    public String trackingParams;
    public LessText lessText;
    public ViewReplies viewReplies;
    public HideReplies hideReplies;
}
