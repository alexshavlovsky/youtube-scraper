package com.ctzn.youtubescraper.model.channelvideos;

import lombok.Value;

@Value
public class VideoDTO {
    String channelId;
    String videoId;
    String title;
    String publishedTimeText;
    int viewCountText;
}
