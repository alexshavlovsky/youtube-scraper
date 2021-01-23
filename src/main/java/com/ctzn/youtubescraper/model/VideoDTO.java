package com.ctzn.youtubescraper.model;

import lombok.Value;

@Value
public class VideoDTO {
    String channelId;
    String videoId;
    String title;
    String publishedTimeText;
    int viewCountText;
}
