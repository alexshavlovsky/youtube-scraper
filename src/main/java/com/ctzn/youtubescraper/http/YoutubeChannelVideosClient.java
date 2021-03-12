package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.model.browsev1.BrowseV1Request;
import com.ctzn.youtubescraper.model.browsev1.BrowseV1Response;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.parser.BrowseApiResponseParser;
import com.ctzn.youtubescraper.parser.json.JsonMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ctzn.youtubescraper.http.IoUtil.*;

public class YoutubeChannelVideosClient extends AbstractYoutubeBrowseClient<VideosGrid> implements IterableHttpClient<VideosGrid> {

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
        BrowseV1Request requestModel = new BrowseV1Request(clientCtx, continuation.getContinuation());
        String requestBody = JsonMapper.asJson(requestModel);

        HttpRequest request = newBrowseApiV1RequestBuilder(requestUri)
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));
        BrowseV1Response browseV1Response = browseApiResponseParser.parseResponseV1Body(body);

        return browseV1Response.getVideosGrid();
    }
}
