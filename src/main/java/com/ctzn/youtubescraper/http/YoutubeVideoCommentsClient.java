package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.ApiResponse;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.parser.CommentApiResponseParser;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.ctzn.youtubescraper.http.IoUtil.*;

@Log
public class YoutubeVideoCommentsClient extends AbstractYoutubeClient<CommentItemSection> {

    private static final CommentApiResponseParser commentApiResponseParser = new CommentApiResponseParser();

    private final String videoId;

    public YoutubeVideoCommentsClient(UserAgentCfg userAgentCfg, String videoId) throws ScraperParserException, ScraperHttpException {
        super(userAgentCfg, uriFactory.newVideoPageUri(videoId), videoPageBodyParser::scrapeInitialCommentItemSection);
        this.videoId = videoId;
        if (!initialData.hasContinuation()) throw new ScraperParserException("Initial comment continuation not found");
    }

    public <T extends ApiResponse> CommentItemSection requestNextSection(NextContinuationData continuationData, RequestUriLengthLimiter limiter, Class<T> valueType) throws ScraperHttpException, ScraperParserException {
        URI requestUri = uriFactory.newCommentApiRequestUri(continuationData);
        limiter.setUriLength(requestUri.toString().length());
        if (limiter.getUriLengthLimitUsagePercent() > 100)
            throw new ScraperHttpException("Request entity size limit is exceeded: no further processing of the section is possible");

        HttpRequest request = HttpRequest.newBuilder(requestUri)
                .headers("User-Agent", userAgentCfg.getUserAgent())
                .headers("Accept", "*/*")
                .headers("Accept-Language", userAgentCfg.getAcceptLanguage())
                .headers("Accept-Encoding", userAgentCfg.getAcceptEncoding())
                .headers("Referer", pageUri)
                .headers("X-YouTube-Client-Name", youtubeCfg.getClientName())
                .headers("X-YouTube-Client-Version", youtubeCfg.getClientVersion())
                .headers("X-YouTube-Device", youtubeCfg.getDevice())
                .headers("X-YouTube-Page-CL", youtubeCfg.getPageCl())
                .headers("X-YouTube-Page-Label", youtubeCfg.getPageLabel())
                .headers("X-YouTube-Utc-Offset", "180")
                .headers("X-YouTube-Time-Zone", "Europe/Minsk")
//              .headers("X-YouTube-Ad-Signals")
                .headers("X-SPF-Referer", pageUri)
                .headers("X-SPF-Previous", pageUri)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Cookie", cookies.getHeader())
                .headers("Origin", "https://www.youtube.com")
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .POST(ofFormData(Map.of(youtubeCfg.getXsrfFieldName(), currentXsrfToken))).build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));
        T commentApiResponse = commentApiResponseParser.parseResponseBody(body, valueType);

        currentXsrfToken = commentApiResponse.getToken();

        return commentApiResponse.getItemSection();
    }

    public String getVideoId() {
        return videoId;
    }

    public NextContinuationData getInitialCommentSectionContinuation() {
        return initialData.getContinuation();
    }
}
