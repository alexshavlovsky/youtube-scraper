package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class DefaultNavigationEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public SignInEndpoint signInEndpoint;
}
