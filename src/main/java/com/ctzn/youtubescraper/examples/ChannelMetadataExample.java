package com.ctzn.youtubescraper.examples;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.http.YoutubeChannelMetadataClient;

public class ChannelMetadataExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging.properties");
    }

    public static void main(String[] args) throws ScraperException {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        YoutubeChannelMetadataClient channelHttpClient = new YoutubeChannelMetadataClient(channelId);
        System.out.println(channelHttpClient.getChannelMetadata());
        System.out.println(channelHttpClient.getChannelMicroformat());
        System.out.println(channelHttpClient.getChannelHeader());
        System.out.println(channelHttpClient.getChannelMetadata().getVanityChannelUrl());
        System.out.println(channelHttpClient.getChannelHeader().getSubscriberCountText());
        System.out.println(channelHttpClient.getChannelVanityName());
    }
}
