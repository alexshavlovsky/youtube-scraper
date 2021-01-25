package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.http.UserAgentCfgFactory;
import com.ctzn.youtubescraper.http.YoutubeChannelMetadataClient;

public class ChannelMetadataExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) throws ScraperParserException, ScraperHttpException {
        String chanelId = "UCksTNgiRyQGwi2ODBie8HdA";
        YoutubeChannelMetadataClient channelHttpClient =
                new YoutubeChannelMetadataClient(UserAgentCfgFactory.getDefaultUserAgentCfg(), chanelId);
        System.out.println(channelHttpClient.getChannelMetadata());
        System.out.println(channelHttpClient.getChannelMicroformat());
        System.out.println(channelHttpClient.getChannelMetadata().getVanityChannelUrl());
        System.out.println(channelHttpClient.getChannelVanityName());
    }
}
