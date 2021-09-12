package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentAbstractFactory;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.core.model.channelmetadata.ChannelHeaderDTO;
import com.ctzn.youtubescraper.core.model.channelmetadata.ChannelMetadata;
import com.ctzn.youtubescraper.core.model.channelmetadata.ChannelMetadataDTO;
import com.ctzn.youtubescraper.core.model.channelmetadata.ChannelMicroformatDTO;
import com.ctzn.youtubescraper.core.parser.ParserUtil;
import lombok.extern.java.Log;

@Log
public class YoutubeChannelMetadataClient extends GenericYoutubeClient<ChannelMetadata> {

    private final String channelId;
    private final String channelVanityName;

    public YoutubeChannelMetadataClient(UserAgentFactory userAgentFactory, String channelId) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        super(userAgentFactory, uriFactory.newChanelPageUri(channelId), videoPageBodyParser::parseChannelMetadata);
        this.channelId = channelId;
        ParserUtil.assertNotNull("Channel metadata not found",
                initialData.metadata.channelMetadataRenderer,
                initialData.microformat.microformatDataRenderer
        );
        channelVanityName = videoPageBodyParser.parseChannelVanityName(getChannelMetadata().getVanityChannelUrl());
    }

    public YoutubeChannelMetadataClient(String channelId) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        this(UserAgentAbstractFactory.getRandomAgentFactory(), channelId);
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelVanityName() {
        return channelVanityName;
    }

    public ChannelMetadataDTO getChannelMetadata() {
        return initialData.metadata.channelMetadataRenderer;
    }

    public ChannelMicroformatDTO getChannelMicroformat() {
        return initialData.microformat.microformatDataRenderer;
    }

    public ChannelHeaderDTO getChannelHeader() {
        return initialData.header.c4TabbedHeaderRenderer;
    }
}
