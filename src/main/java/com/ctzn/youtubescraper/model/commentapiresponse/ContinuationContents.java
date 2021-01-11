package com.ctzn.youtubescraper.model.commentapiresponse;

import lombok.Value;
import com.ctzn.youtubescraper.model.commentitemsection.CommentItemSection;

@Value
class ContinuationContents{
    public CommentItemSection itemSectionContinuation;
}
