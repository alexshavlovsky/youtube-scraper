package model.commentitemsection;

import lombok.Data;

@Data
class NextEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public WatchEndpoint watchEndpoint;
}
