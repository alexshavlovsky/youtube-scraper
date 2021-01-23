package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;

public class YoutubeChannelVideosClient extends AbstractYoutubeClient<VideosGrid> {

    private final String channelId;
    private final String channelVanityName;

    public YoutubeChannelVideosClient(UserAgentCfg userAgentCfg, String channelId, String channelVanityName) throws ScraperHttpException, ScraperParserException {
        super(userAgentCfg, uriFactory.newChannelVideosPageUri(channelVanityName), videoPageBodyParser::parseVideosGrid);
        this.channelId = channelId;
        this.channelVanityName = channelVanityName;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelVanityName() {
        return channelVanityName;
    }

    public VideosGrid getInitialGrid() {
        return initialData;
    }
}
