package com.ctzn.youtubescraper.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class YoutubeCfgDTO {
    @JsonProperty("PAGE_CL")
    String pageCl;
    @JsonProperty("XSRF_TOKEN")
    String xsrfToken;
    @JsonProperty("XSRF_FIELD_NAME")
    String xsrfFieldName;
    @JsonProperty("DEVICE")
    String device;
    @JsonProperty("PAGE_BUILD_LABEL")
    String pageLabel;
    @JsonProperty("INNERTUBE_CONTEXT_CLIENT_NAME")
    String clientName;
    @JsonProperty("INNERTUBE_CONTEXT_CLIENT_VERSION")
    String clientVersion;
    @JsonProperty("INNERTUBE_API_KEY")
    String apiKey;
    @JsonProperty("VISITOR_DATA")
    String visitorData;
}
