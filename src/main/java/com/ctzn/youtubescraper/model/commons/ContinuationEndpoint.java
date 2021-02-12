package com.ctzn.youtubescraper.model.commons;

public class ContinuationEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public ContinuationCommand continuationCommand;

    public NextContinuationData asContinuation() {
        return new NextContinuationData(continuationCommand.token, clickTrackingParams);
    }
}
