package com.ctzn.youtubescraper.model.channelvideos;

import com.ctzn.youtubescraper.model.channelmetadata.ChannelMetadata;
import com.ctzn.youtubescraper.parser.json.JsonUnwrapProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BrowseResponse extends ChannelMetadata {
    @JsonProperty("continuationContents")
    @JsonUnwrapProperty("gridContinuation")
    public VideosGrid grid;
}
