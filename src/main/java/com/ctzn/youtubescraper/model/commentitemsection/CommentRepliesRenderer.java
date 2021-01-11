package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

import java.util.List;

@Value
class CommentRepliesRenderer {
    public List<Continuation> continuations;
    public MoreText moreText;
    public String trackingParams;
    public LessText lessText;
    public ViewReplies viewReplies;
    public HideReplies hideReplies;
}
