package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.model.ApiResponse;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.YoutubeCfgDTO;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.parser.CommentApiResponseParser;
import com.ctzn.youtubescraper.parser.VideoPageBodyParser;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static com.ctzn.youtubescraper.http.IoUtil.*;

@Log
public class YoutubeHttpClient {

    private static final CommentApiResponseParser commentApiResponseParser = new CommentApiResponseParser();
    private static final YoutubeUriFactory uriFactory = new YoutubeUriFactory();

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private final CustomCookieManager cookies = new CustomCookieManager("youtube.com");
    private final UserAgentCfg userAgentCfg;
    private final String videoId;
    private final String videoPageUri;
    private final YoutubeCfgDTO youtubeCfg;
    private final NextContinuationData initialCommentSectionContinuation;

    private String currentXsrfToken;

    public YoutubeHttpClient(UserAgentCfg userAgentCfg, String videoId) throws Exception {
        this.videoId = videoId;
        this.userAgentCfg = userAgentCfg;
        this.videoPageUri = uriFactory.newVideoPageUri(videoId);
        VideoPageBodyParser videoPageBodyParser = new VideoPageBodyParser();
        log.info(() -> String.format("Fetch video page: [%s]", videoPageUri));
        String body = fetchVideoPage();
        log.fine(() -> "Scrape initial youtube context");
        youtubeCfg = videoPageBodyParser.scrapeYoutubeConfig(body);
        currentXsrfToken = youtubeCfg.getXsrfToken();
        if (currentXsrfToken == null) throw new Exception("Initial XSRF token not found");
        log.fine(() -> "Scrape comment section continuation");
        CommentItemSection commentItemSection = videoPageBodyParser.scrapeInitialCommentItemSection(body);
        if (commentItemSection.hasContinuation())
            initialCommentSectionContinuation = commentItemSection.nextContinuation();
        else throw new Exception("Initial comment continuation not found");
    }

    private String fetchVideoPage() throws IOException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(videoPageUri))
                .headers("User-Agent", userAgentCfg.getUserAgent())
                .headers("Accept", userAgentCfg.getAccept())
                .headers("Accept-Language", userAgentCfg.getAcceptLanguage())
                .headers("Accept-Encoding", userAgentCfg.getAcceptEncoding())
                .headers("DNT", "1")
                .headers("Upgrade-Insecure-Requests", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .GET().build();

        HttpResponse<InputStream> httpResponse = completeRequest(httpClient, request);

        cookies.put(httpResponse.headers());
        cookies.put("PREF=f4=4000000");

        return readStreamToString(applyBrotliDecoderAndGetBody(httpResponse));
    }

    public <T extends ApiResponse> CommentItemSection requestNextSection(NextContinuationData continuationData, RequestUriLengthLimiter limiter, Class<T> valueType) throws Exception {
        URI requestUri = uriFactory.newCommentApiRequestUri(continuationData);
        limiter.setUriLength(requestUri.toString().length());
        if (limiter.getUriLengthLimitUsagePercent() > 100)
            throw new Exception("Request entity size limit is exceeded: no further processing of the section is possible");

        HttpRequest request = HttpRequest.newBuilder(requestUri)
                .headers("User-Agent", userAgentCfg.getUserAgent())
                .headers("Accept", "*/*")
                .headers("Accept-Language", userAgentCfg.getAcceptLanguage())
                .headers("Accept-Encoding", userAgentCfg.getAcceptEncoding())
                .headers("Referer", videoPageUri)
                .headers("X-YouTube-Client-Name", youtubeCfg.getClientName())
                .headers("X-YouTube-Client-Version", youtubeCfg.getClientVersion())
                .headers("X-YouTube-Device", youtubeCfg.getDevice())
                .headers("X-YouTube-Page-CL", youtubeCfg.getPageCl())
                .headers("X-YouTube-Page-Label", youtubeCfg.getPageLabel())
                .headers("X-YouTube-Utc-Offset", "180")
                .headers("X-YouTube-Time-Zone", "Europe/Minsk")
//              .headers("X-YouTube-Ad-Signals")
                .headers("X-SPF-Referer", videoPageUri)
                .headers("X-SPF-Previous", videoPageUri)
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
        return initialCommentSectionContinuation;
    }
}
