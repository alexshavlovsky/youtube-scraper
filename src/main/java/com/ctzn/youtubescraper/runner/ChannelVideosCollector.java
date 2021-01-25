package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.handler.VideoCollector;
import com.ctzn.youtubescraper.http.UserAgentCfg;
import com.ctzn.youtubescraper.http.UserAgentCfgFactory;
import com.ctzn.youtubescraper.http.YoutubeChannelMetadataClient;
import com.ctzn.youtubescraper.http.YoutubeChannelVideosClient;
import com.ctzn.youtubescraper.iterator.IterableVideoContext;
import com.ctzn.youtubescraper.iterator.VideoContext;
import com.ctzn.youtubescraper.iterator.VideoContextIterator;
import com.ctzn.youtubescraper.model.ChannelDTO;
import lombok.extern.java.Log;

import java.util.List;
import java.util.concurrent.Callable;

@Log
public class ChannelVideosCollector implements Callable<ChannelDTO> {

    private final String channelId;
    private final VideoCollector handler;
    private final UserAgentCfg userAgentCfg;

    public ChannelVideosCollector(String channelId) {
        this.channelId = channelId;
        this.handler = new VideoCollector();
        userAgentCfg = UserAgentCfgFactory.getDefaultUserAgentCfg();
    }

    @Override
    public ChannelDTO call() throws ScraperException {
        try {
            YoutubeChannelMetadataClient metadataClient = new YoutubeChannelMetadataClient(userAgentCfg, channelId);
            YoutubeChannelVideosClient videosClient = new YoutubeChannelVideosClient(userAgentCfg, channelId, metadataClient.getChannelVanityName());
            // TODO scrape subscribers count from the page header
            //"header":{
            //      "c4TabbedHeaderRenderer":{
            //         "channelId":"...",
            //         "title":"...",
            //         "avatar":{...},
            //         "subscriberCountText":{
            //            "simpleText":"11 subscribers"
            //         },
            //      }
            //}
            IterableVideoContext videosContext = new VideoContext(videosClient);
            VideoContextIterator iterator = new VideoContextIterator(videosContext, List.of(handler));
            iterator.traverse();
            log.info("DONE: " + videosContext.getShortResultStat());
            return new ChannelDTO(channelId, metadataClient.getChannelVanityName(), handler.getVideos());
        } catch (ScraperException e) {
            log.warning("FAILED: " + e.toString());
            throw e;
        }
    }
}
