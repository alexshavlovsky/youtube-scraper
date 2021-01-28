package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.BrowseApiResponse;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.parser.BrowseApiResponseParser;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ctzn.youtubescraper.http.IoUtil.*;

public class YoutubeChannelVideosClient extends AbstractYoutubeClient<VideosGrid> {

    private static final BrowseApiResponseParser browseApiResponseParser = new BrowseApiResponseParser();

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

    public VideosGrid requestNextSection(NextContinuationData continuationData) throws ScraperHttpException, ScraperParserException {
        URI requestUri = uriFactory.newBrowseApiRequestUri(continuationData);

        HttpRequest request = newApiRequestBuilder(requestUri).GET().build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));
        BrowseApiResponse browseApiResponse = browseApiResponseParser.parseResponseBody(body);

        currentXsrfToken = browseApiResponse.getToken();

        return browseApiResponse.getVideosGrid();
    }
}
