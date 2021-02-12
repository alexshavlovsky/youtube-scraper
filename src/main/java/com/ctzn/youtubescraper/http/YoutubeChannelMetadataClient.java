package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMetadata;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMetadataDTO;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMicroformatDTO;
import com.ctzn.youtubescraper.parser.ParserUtil;
import lombok.extern.java.Log;

@Log
public class YoutubeChannelMetadataClient extends AbstractYoutubeClient<ChannelMetadata> {

    private final String channelId;
    private final String channelVanityName;

    public YoutubeChannelMetadataClient(UserAgentCfg userAgentCfg, String channelId) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        super(userAgentCfg, uriFactory.newChanelPageUri(channelId), videoPageBodyParser::parseChannelMetadata);
        this.channelId = channelId;
        ParserUtil.assertNotNull("Channel metadata not found",
                initialData.metadata.channelMetadataRenderer,
                initialData.microformat.microformatDataRenderer
        );
        channelVanityName = videoPageBodyParser.parseChannelVanityName(getChannelMetadata().getVanityChannelUrl());
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
}
