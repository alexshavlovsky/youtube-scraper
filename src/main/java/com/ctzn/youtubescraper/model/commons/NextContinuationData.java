package com.ctzn.youtubescraper.model.commons;

import lombok.Value;

@Value
public class NextContinuationData {
    String continuation;
    String clickTrackingParams;
}
