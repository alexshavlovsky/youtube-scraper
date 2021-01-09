package model.commentitemsection;

import lombok.Data;

@Data
class CommentActionButtonsRenderer {
    public LikeButton likeButton;
    public ReplyButton replyButton;
    public DislikeButton dislikeButton;
    public String trackingParams;
    public String style;
}
