package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class AuthorEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public BrowseEndpoint browseEndpoint;
}
