package com.ctzn.youtubescraper.core.model.commons;

import lombok.Value;

@Value
public class NextContinuationData {
    String continuation;
    String clickTrackingParams;
}
