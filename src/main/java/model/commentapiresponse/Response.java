package model.commentapiresponse;

import lombok.Data;

@Data
class Response{
    public ContinuationContents continuationContents;
    public String trackingParams;
}
