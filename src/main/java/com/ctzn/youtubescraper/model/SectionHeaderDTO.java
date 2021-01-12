package com.ctzn.youtubescraper.model;

import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.Value;

@Value
public class SectionHeaderDTO {
    public NextContinuationData orderTopFirst;
    public NextContinuationData orderNewestFirst;
    public String commentsCountText;
}
