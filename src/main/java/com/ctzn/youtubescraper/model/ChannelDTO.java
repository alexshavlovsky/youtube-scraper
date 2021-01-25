package com.ctzn.youtubescraper.model;

import lombok.Value;

import java.util.List;

@Value
public class ChannelDTO {
    public String channelId;
    public String channelVanityName;
    public List<VideoDTO> videos;
}
