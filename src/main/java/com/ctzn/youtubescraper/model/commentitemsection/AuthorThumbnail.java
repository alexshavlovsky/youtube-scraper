package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

import java.util.List;

@Value
class AuthorThumbnail {
    public List<Thumbnail> thumbnails;
    public Accessibility accessibility;
}
