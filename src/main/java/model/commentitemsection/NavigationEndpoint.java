package model.commentitemsection;

import lombok.Data;

@Data
class NavigationEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public UrlEndpoint urlEndpoint;
    public WatchEndpoint watchEndpoint;
    public SignInEndpoint signInEndpoint;
}
