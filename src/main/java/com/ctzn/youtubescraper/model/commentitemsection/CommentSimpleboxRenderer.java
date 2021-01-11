package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class CommentSimpleboxRenderer {
    public AuthorThumbnail authorThumbnail;
    public PlaceholderText placeholderText;
    public PrepareAccountEndpoint prepareAccountEndpoint;
    public String trackingParams;
    public String avatarSize;
}
