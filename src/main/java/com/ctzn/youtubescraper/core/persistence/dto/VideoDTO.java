package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

import java.util.Date;

@Value
public class VideoDTO {
    public String channelId;
    public String videoId;
    public String title;
    public String publishedTimeText;
    public Date publishedDate;
    public int viewCountText;
}
