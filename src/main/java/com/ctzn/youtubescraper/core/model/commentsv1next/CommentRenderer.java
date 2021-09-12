package com.ctzn.youtubescraper.core.model.commentsv1next;

import com.ctzn.youtubescraper.core.model.commons.SimpleText;
import com.ctzn.youtubescraper.core.model.commons.Text;
import com.ctzn.youtubescraper.core.model.commons.Thumbnails;

public class CommentRenderer {
    public SimpleText authorText;
    public Thumbnails authorThumbnail;
    public AuthorEndpoint authorEndpoint;
    public Text contentText;
    public Text publishedTimeText;
    public boolean isLiked;
    public String commentId;
    public ActionButtons actionButtons;
    public boolean authorIsChannelOwner;
    public String voteStatus;
    public String trackingParams;
    public SimpleText voteCount;
    public ExpandButton expandButton;
    public CollapseButton collapseButton;
    public int replyCount;
}
