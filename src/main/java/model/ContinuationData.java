package model;

import lombok.Data;

@Data
public class ContinuationData {
    public final String continuation;
    public final String clickTrackingParams;

    public ContinuationData(String continuation, String clickTrackingParams) {
        this.continuation = continuation;
        this.clickTrackingParams = clickTrackingParams;
    }
}
