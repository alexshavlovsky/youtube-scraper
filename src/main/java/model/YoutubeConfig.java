package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeConfig {
    @JsonProperty("PAGE_CL")
    public String PAGE_CL;
    @JsonProperty("XSRF_TOKEN")
    public String XSRF_TOKEN;
    @JsonProperty("XSRF_FIELD_NAME")
    public String XSRF_FIELD_NAME;
    @JsonProperty("DEVICE")
    public String DEVICE;
    @JsonProperty("PAGE_BUILD_LABEL")
    public String PAGE_BUILD_LABEL;
    @JsonProperty("INNERTUBE_CONTEXT_CLIENT_NAME")
    public String INNERTUBE_CONTEXT_CLIENT_NAME;
    @JsonProperty("INNERTUBE_CONTEXT_CLIENT_VERSION")
    public String INNERTUBE_CONTEXT_CLIENT_VERSION;
}
