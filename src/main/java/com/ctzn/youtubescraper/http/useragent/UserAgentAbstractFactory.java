package com.ctzn.youtubescraper.http.useragent;

import java.util.Random;

public class UserAgentAbstractFactory {

    private static final UserAgentCfg USER_AGENT_FIREFOX_84 = new UserAgentCfg(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "en-US,en;q=0.5",
            "gzip, deflate, br"
    );

    private static final UserAgentCfg USER_AGENT_FIREFOX_86 = new UserAgentCfg(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "en,en-US;q=0.5",
            "gzip, deflate, br"
    );

    private static final UserAgentCfg USER_AGENT_CHROME = new UserAgentCfg(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "en-US;q=0.9,en;q=0.8",
            "gzip, deflate, br"
    );

    private static final UserAgentCfg[] agents = {USER_AGENT_CHROME, USER_AGENT_FIREFOX_84, USER_AGENT_FIREFOX_86};

    private static UserAgentCfg getRandom() {
        return agents[new Random().nextInt(agents.length)];
    }

    private static UserAgentCfg getDefault() {
        return USER_AGENT_FIREFOX_84;
    }

    public static UserAgentFactory getRandomAgentFactory() {
        return UserAgentAbstractFactory::getRandom;
    }

    public static UserAgentFactory getDefaultAgentFactory() {
        return UserAgentAbstractFactory::getDefault;
    }

}
