package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YoutubeConfig {
    @JsonProperty("PAGE_CL")
    public String pageCl;
    @JsonProperty("XSRF_TOKEN")
    public String xsrfToken;
    @JsonProperty("XSRF_FIELD_NAME")
    public String xsrfFieldName;
    @JsonProperty("DEVICE")
    public String device;
    @JsonProperty("PAGE_BUILD_LABEL")
    public String pageLabel;
    @JsonProperty("INNERTUBE_CONTEXT_CLIENT_NAME")
    public String clientName;
    @JsonProperty("INNERTUBE_CONTEXT_CLIENT_VERSION")
    public String clientVersion;
}
