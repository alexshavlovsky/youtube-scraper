package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.http.UserAgentCfg;
import com.ctzn.youtubescraper.http.YoutubeChannelMetadataClient;

public class ChannelMetadataExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    private static final UserAgentCfg DEFAULT_USER_AGENT_CONTEXT = new UserAgentCfg(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "en-US,en;q=0.5",
            "gzip, deflate, br"
    );

    public static void main(String[] args) throws ScraperParserException, ScraperHttpException {
        String chanelId = "UCksTNgiRyQGwi2ODBie8HdA";
        YoutubeChannelMetadataClient channelHttpClient = new YoutubeChannelMetadataClient(DEFAULT_USER_AGENT_CONTEXT, chanelId);
        System.out.println(channelHttpClient.getChannelMetadata());
        System.out.println(channelHttpClient.getChannelMicroformat());
        System.out.println(channelHttpClient.getChannelMetadata().getVanityChannelUrl());
        System.out.println(channelHttpClient.getChannelVanityName());
    }
}
