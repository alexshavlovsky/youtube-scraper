package com.ctzn.youtubescraper.http.useragent;

import lombok.Value;

@Value
public class UserAgentCfg {
    String userAgent;
    String accept;
    String acceptLanguage;
    String acceptEncoding;
}
