package com.ctzn.youtubescraper.model.commentapiresponse;

import lombok.Value;

@Value
class Response{
    public ContinuationContents continuationContents;
    public String trackingParams;
}
