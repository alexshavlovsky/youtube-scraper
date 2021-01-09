package model.commentitemsection;

import lombok.Data;

@Data
class DefaultNavigationEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public SignInEndpoint signInEndpoint;
}
