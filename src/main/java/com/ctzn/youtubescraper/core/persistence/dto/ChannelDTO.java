package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

@Value
public class ChannelDTO {
    public String channelId;
    public String channelVanityName;
    public String title;
    public Integer videoCount;
    public Long subscriberCount;
}
