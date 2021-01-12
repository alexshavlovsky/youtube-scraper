package com.ctzn.youtubescraper.model.continuation;

import lombok.Value;

@Value
public class NextContinuationData {
    public String continuation;
    public String clickTrackingParams;
}
