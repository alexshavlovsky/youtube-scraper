package com.ctzn.youtubescraper.core.model.browsev1;

import lombok.Value;

@Value
public class V1ApiRequest {
    public ClientContext context;
    public String continuation;
}
