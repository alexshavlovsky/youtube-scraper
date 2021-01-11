package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class CommentActionButtonsRenderer {
    public LikeButton likeButton;
    public ReplyButton replyButton;
    public DislikeButton dislikeButton;
    public String trackingParams;
    public String style;
}
