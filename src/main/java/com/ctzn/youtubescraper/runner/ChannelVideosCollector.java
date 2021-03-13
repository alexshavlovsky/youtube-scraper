package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.handler.DataCollector;
import com.ctzn.youtubescraper.http.IterableHttpClient;
import com.ctzn.youtubescraper.http.YoutubeChannelMetadataClient;
import com.ctzn.youtubescraper.http.YoutubeChannelVideosClient;
import com.ctzn.youtubescraper.http.useragent.UserAgentAbstractFactory;
import com.ctzn.youtubescraper.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.iterator.video.VideoContext;
import com.ctzn.youtubescraper.model.channelvideos.ChannelDTO;
import com.ctzn.youtubescraper.model.channelvideos.VideoDTO;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import lombok.extern.java.Log;

import java.util.concurrent.Callable;

@Log
public class ChannelVideosCollector implements Callable<ChannelDTO> {

    private final String channelId;
    private final DataCollector<VideoDTO> handler = new DataCollector<>();
    private final UserAgentFactory userAgentFactory;

    public ChannelVideosCollector(String channelId) {
        this.channelId = channelId;
        userAgentFactory = UserAgentAbstractFactory.getRandomAgentFactory();
    }

    @Override
    public ChannelDTO call() throws ScraperException {
        try {
            YoutubeChannelMetadataClient metadataClient = new YoutubeChannelMetadataClient(userAgentFactory, channelId);
            IterableHttpClient<VideosGrid> videosClient = new YoutubeChannelVideosClient(userAgentFactory, channelId, metadataClient.getChannelVanityName());
            VideoContext videosContext = new VideoContext(videosClient);
            videosContext.traverse(handler);
            log.info("DONE " + videosContext.getShortResultStat());
            return new ChannelDTO(channelId,
                    metadataClient.getChannelVanityName(),
                    metadataClient.getChannelHeader().getTitle(),
                    handler.size(),
                    metadataClient.getChannelHeader().getSubscriberCount(),
                    handler
            );
        } catch (ScrapperInterruptedException e) {
            log.info("INTERRUPTED " + channelId + ": " + e.toString());
            throw e;
        } catch (ScraperException e) {
            log.warning("FAILED " + channelId + ": " + e.toString());
            throw e;
        }
    }
}
