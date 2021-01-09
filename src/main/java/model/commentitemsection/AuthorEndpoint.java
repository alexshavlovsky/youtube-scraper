package model.commentitemsection;

import lombok.Data;

@Data
class AuthorEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public BrowseEndpoint browseEndpoint;
}
