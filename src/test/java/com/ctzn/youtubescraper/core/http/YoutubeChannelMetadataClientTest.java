package com.ctzn.youtubescraper.core.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class YoutubeChannelMetadataClientTest {

    @Test
    void testIntegration() {
        assertDoesNotThrow(() -> {
            String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
            YoutubeChannelMetadataClient channelHttpClient = new YoutubeChannelMetadataClient(channelId);
            assertEquals("c/bienadam", channelHttpClient.getChannelVanityName());
        });
    }

}
