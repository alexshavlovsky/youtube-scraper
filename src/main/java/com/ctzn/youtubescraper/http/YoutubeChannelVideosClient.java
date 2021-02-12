package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.model.channelvideos.BrowseApiResponse;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.parser.BrowseApiResponseParser;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ctzn.youtubescraper.http.IoUtil.*;

@Deprecated
public class YoutubeChannelVideosClient extends AbstractYoutubeClient<VideosGrid> implements IterableHttpClient<VideosGrid> {

    private static final BrowseApiResponseParser browseApiResponseParser = new BrowseApiResponseParser();

    private final String channelId;
    private final String channelVanityName;

    public YoutubeChannelVideosClient(UserAgentCfg userAgentCfg, String channelId, String channelVanityName) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        super(userAgentCfg, uriFactory.newChannelVideosPageUri(channelVanityName), videoPageBodyParser::parseVideosGrid);
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
    public VideosGrid requestNext(NextContinuationData continuation) throws ScrapperInterruptedException, ScraperHttpException, ScraperParserException {
        URI requestUri = uriFactory.newBrowseApiRequestUri(continuation);

        HttpRequest request = newApiRequestBuilder(requestUri).GET().build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));
        BrowseApiResponse browseApiResponse = browseApiResponseParser.parseResponseBody(body);

        currentXsrfToken = browseApiResponse.getToken();

        return browseApiResponse.getVideosGrid();
    }
}
