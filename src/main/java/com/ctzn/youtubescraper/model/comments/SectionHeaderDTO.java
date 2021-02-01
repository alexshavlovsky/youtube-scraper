package com.ctzn.youtubescraper.model.comments;

import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.Value;

@Value
public class SectionHeaderDTO {
    NextContinuationData orderTopFirst;
    NextContinuationData orderNewestFirst;
    String commentsCountText;
}
