package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.exception.ScraperException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.handler.DataCollector;
import com.ctzn.youtubescraper.core.http.IterableHttpClient;
import com.ctzn.youtubescraper.core.http.YoutubeChannelMetadataClient;
import com.ctzn.youtubescraper.core.http.YoutubeChannelVideosClient;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentAbstractFactory;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.core.iterator.video.VideoContext;
import com.ctzn.youtubescraper.core.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelDTO;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelVideosDTO;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
import lombok.extern.java.Log;

import java.util.concurrent.Callable;

@Log
public class ChannelVideosCollector implements Callable<ChannelVideosDTO> {

    private final String channelId;
    private final DataCollector<VideoDTO> handler = new DataCollector<>();
    private final UserAgentFactory userAgentFactory;

    public ChannelVideosCollector(String channelId) {
        this.channelId = channelId;
        userAgentFactory = UserAgentAbstractFactory.getRandomAgentFactory();
    }

    @Override
    public ChannelVideosDTO call() throws ScraperException {
        try {
            YoutubeChannelMetadataClient metadataClient = new YoutubeChannelMetadataClient(userAgentFactory, channelId);
            IterableHttpClient<VideosGrid> videosClient = new YoutubeChannelVideosClient(userAgentFactory, channelId, metadataClient.getChannelVanityName());
            VideoContext videosContext = new VideoContext(videosClient);
            videosContext.traverse(handler);
            log.info("DONE " + videosContext.getShortResultStat());
            return new ChannelVideosDTO(
                    new ChannelDTO(channelId,
                            metadataClient.getChannelVanityName(),
                            metadataClient.getChannelHeader().getTitle(),
                            handler.size(),
                            metadataClient.getChannelHeader().getSubscriberCount()),
                    handler);
        } catch (ScrapperInterruptedException e) {
            log.info("INTERRUPTED " + channelId + ": " + e.toString());
            throw e;
        } catch (ScraperException e) {
            log.warning("FAILED " + channelId + ": " + e.toString());
            throw e;
        }
    }

}
