package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMetadata;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMetadataDTO;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMicroformatDTO;
import com.ctzn.youtubescraper.parser.ParserUtil;
import lombok.extern.java.Log;

@Log
public class YoutubeChannelMetadataClient extends AbstractYoutubeClient<ChannelMetadata> {

    private final String channelId;

    public YoutubeChannelMetadataClient(UserAgentCfg userAgentCfg, String channelId) throws ScraperHttpException, ScraperParserException {
        super(userAgentCfg, uriFactory.newChanelPageUri(channelId), videoPageBodyParser::parseChannelMetadata);
        this.channelId = channelId;
        ParserUtil.assertNotNull("Channel metadata not found",
                initialData.metadata,
                initialData.metadata.channelMetadataRenderer,
                initialData.microformat,
                initialData.microformat.microformatDataRenderer
        );
    }

    public String getChannelId() {
        return channelId;
    }

    public ChannelMetadataDTO getChannelMetadata() {
        return initialData.metadata.channelMetadataRenderer;
    }

    public ChannelMicroformatDTO getChannelMicroformat() {
        return initialData.microformat.microformatDataRenderer;
    }
}
