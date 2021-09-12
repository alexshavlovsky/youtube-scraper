package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.core.model.browsev1.V1ApiRequest;
import com.ctzn.youtubescraper.core.model.browsev1.BrowseV1Response;
import com.ctzn.youtubescraper.core.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.parser.BrowseApiResponseParser;
import com.ctzn.youtubescraper.core.parser.json.JsonMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ctzn.youtubescraper.core.http.IoUtil.*;

public class YoutubeChannelVideosClient extends GenericYoutubeV1Client<VideosGrid> implements IterableHttpClient<VideosGrid> {

    private static final BrowseApiResponseParser browseApiResponseParser = new BrowseApiResponseParser();

    private final String channelId;
    private final String channelVanityName;

    public YoutubeChannelVideosClient(UserAgentFactory userAgentFactory, String channelId, String channelVanityName) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        super(userAgentFactory, uriFactory.newChannelVideosPageUri(channelVanityName), videoPageBodyParser::parseVideosGrid);
        this.channelId = channelId;
        this.channelVanityName = channelVanityName;
    }

    public String getChannelVanityName() {
        return channelVanityName;
    }

    @Override
    public String getParentId() {
        return channelId;
    }

    @Override
    public VideosGrid getInitial() {
        return initialData;
    }

    @Override
    public VideosGrid requestNext(NextContinuationData continuation) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        URI requestUri = uriFactory.newBrowseApiV1RequestUri(youtubeCfg);

        clientCtx.clickTracking.clickTrackingParams = continuation.getClickTrackingParams();
        V1ApiRequest requestModel = new V1ApiRequest(clientCtx, continuation.getContinuation());
        String requestBody = JsonMapper.asJson(requestModel);

        HttpRequest request = newV1ApiRequestBuilder(requestUri)
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));
        BrowseV1Response browseV1Response = browseApiResponseParser.parseResponseV1Body(body);

        return browseV1Response.getVideosGrid();
    }
}
