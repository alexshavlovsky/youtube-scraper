package com.ctzn.youtubescraper.http;

public class UserAgentCfgFactory {
    private static final UserAgentCfg DEFAULT_USER_AGENT_CONFIG = new UserAgentCfg(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "en-US,en;q=0.5",
            "gzip, deflate, br"
    );

    // TODO: pass an UserAgentFactory instance to a http client constructor instead of UserAgentCfg
    // that will enable to inject a random user agent into new http context
//    private static final UserAgentCfg DEFAULT_USER_AGENT_CHROME = new UserAgentCfg(
//            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36",
//            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
//            "en-US;q=0.9,en;q=0.8",
//            "gzip, deflate, br"
//    );

    public static UserAgentCfg getDefaultUserAgentCfg() {
        return DEFAULT_USER_AGENT_CONFIG;
    }
}
