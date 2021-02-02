package com.ctzn.youtubescraper.model.channelmetadata;

import com.ctzn.youtubescraper.parser.json.JsonUnwrapProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelMetadata {
    @JsonProperty("metadata")
    @JsonUnwrapProperty("channelMetadataRenderer")
    public ChannelMetadataDTO channelMetadata;
    @JsonProperty("microformat")
    @JsonUnwrapProperty("microformatDataRenderer")
    public ChannelMicroformatDTO channelMicroformat;
}
