package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

@Value
public class VideoDTO {
    String channelId;
    String videoId;
    String title;
    String publishedTimeText;
    int viewCountText;
}
