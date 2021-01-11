package com.ctzn.youtubescraper.model;

import lombok.Value;

@Value
public class CommentThreadHeader {
    public ContinuationData orderTopFirst;
    public ContinuationData orderNewestFirst;
    public String commentsCountText;
}
