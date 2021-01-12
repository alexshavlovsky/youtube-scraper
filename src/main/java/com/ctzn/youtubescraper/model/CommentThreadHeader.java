package com.ctzn.youtubescraper.model;

import com.ctzn.youtubescraper.model.continuation.NextContinuationData;
import lombok.Value;

@Value
public class CommentThreadHeader {
    public NextContinuationData orderTopFirst;
    public NextContinuationData orderNewestFirst;
    public String commentsCountText;
}
