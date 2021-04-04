package com.ctzn.youtubescraper.core.http.useragent;

import lombok.Value;

@Value
public class UserAgentCfg {
    String userAgent;
    String accept;
    String acceptLanguage;
    String acceptEncoding;
}
