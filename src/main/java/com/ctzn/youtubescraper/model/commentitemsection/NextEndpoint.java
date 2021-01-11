package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class NextEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public WatchEndpoint watchEndpoint;
}
