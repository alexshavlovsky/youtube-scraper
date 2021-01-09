package model.commentitemsection;

import lombok.Data;

@Data
class PrepareAccountEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public SignInEndpoint signInEndpoint;
}
