package model.commentitemsection;

import lombok.Data;

@Data
class CommentSimpleboxRenderer {
    public AuthorThumbnail authorThumbnail;
    public PlaceholderText placeholderText;
    public PrepareAccountEndpoint prepareAccountEndpoint;
    public String trackingParams;
    public String avatarSize;
}
