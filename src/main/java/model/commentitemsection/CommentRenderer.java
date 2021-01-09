package model.commentitemsection;

import lombok.Data;

@Data
class CommentRenderer {
    public AuthorText authorText;
    public AuthorThumbnail authorThumbnail;
    public AuthorEndpoint authorEndpoint;
    public ContentText contentText;
    public PublishedTimeText publishedTimeText;
    public boolean isLiked;
    public int likeCount;
    public String commentId;
    public ActionButtons actionButtons;
    public boolean authorIsChannelOwner;
    public String voteStatus;
    public String trackingParams;
    public VoteCount voteCount;
    public ExpandButton expandButton;
    public CollapseButton collapseButton;
    public int replyCount;
    public LoggingDirectives loggingDirectives;
    public AuthorCommentBadge authorCommentBadge;
}
