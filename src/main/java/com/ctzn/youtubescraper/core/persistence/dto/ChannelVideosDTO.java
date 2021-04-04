package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

import java.util.List;

@Value
public class ChannelVideosDTO {
    ChannelDTO channel;
    List<VideoDTO> videos;
}
