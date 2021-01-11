package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class NextContinuationData {
    public String continuation;
    public String clickTrackingParams;
    public Label label;
}
