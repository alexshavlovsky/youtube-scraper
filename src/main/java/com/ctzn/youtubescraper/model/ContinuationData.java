package com.ctzn.youtubescraper.model;

import lombok.Value;

@Value
public class ContinuationData {
    public String continuation;
    public String clickTrackingParams;
}
