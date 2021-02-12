package com.ctzn.youtubescraper.model.browsev1;

import lombok.Value;

@Value
public class BrowseV1Request {
    public ClientContext context;
    public String continuation;
}
