package com.ctzn.youtubescraper.core.model.comments;

import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import lombok.Value;

@Value
public class SectionHeaderDTO {
    NextContinuationData orderTopFirst;
    NextContinuationData orderNewestFirst;
    String commentsCountText;
}
