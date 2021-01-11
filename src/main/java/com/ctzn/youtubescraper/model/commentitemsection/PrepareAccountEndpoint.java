package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class PrepareAccountEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public SignInEndpoint signInEndpoint;
}
