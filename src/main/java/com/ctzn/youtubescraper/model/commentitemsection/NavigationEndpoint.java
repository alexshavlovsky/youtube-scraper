package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class NavigationEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public UrlEndpoint urlEndpoint;
    public WatchEndpoint watchEndpoint;
    public SignInEndpoint signInEndpoint;
}
