package com.ctzn.youtubescraper.model.channelvideos;

import lombok.Value;

import java.util.List;

@Value
public class ChannelDTO {
    public String channelId;
    public String channelVanityName;
    public List<VideoDTO> videos;
}
