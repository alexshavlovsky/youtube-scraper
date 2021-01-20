package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.YoutubeCfgDTO;
import com.ctzn.youtubescraper.parser.VideoPageBodyParser;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ctzn.youtubescraper.http.IoUtil.*;

@Log
abstract class AbstractYoutubeClient<E> {
    static final YoutubeUriFactory uriFactory = new YoutubeUriFactory();
    static final VideoPageBodyParser videoPageBodyParser = new VideoPageBodyParser();
    final YoutubeCfgDTO youtubeCfg;
    final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    final CustomCookieManager cookies = new CustomCookieManager("youtube.com");
    final UserAgentCfg userAgentCfg;
    final String pageUri;
    final E initialData;
    String currentXsrfToken;

    AbstractYoutubeClient(UserAgentCfg userAgentCfg, String pageUri, YoutubeInitialDataHandler<E> youtubeInitialDataHandler) throws ScraperHttpException, ScraperParserException {
        this.userAgentCfg = userAgentCfg;
        this.pageUri = pageUri;
        log.info(() -> String.format("Fetch page: [%s]", pageUri));
        String body = fetchPage();
        log.fine(() -> "Scrape initial youtube config");
        youtubeCfg = videoPageBodyParser.scrapeYoutubeConfig(body);
        currentXsrfToken = youtubeCfg.getXsrfToken();
        if (currentXsrfToken == null) throw new ScraperParserException("Initial XSRF token not found");
        log.fine(() -> "Scrape initial youtube data");
        String ytInitialDataJson = videoPageBodyParser.scrapeYtInitialDataJson(body);
        log.fine(() -> "Parse  initial youtube data");
        initialData = youtubeInitialDataHandler.handle(ytInitialDataJson);
    }

    private String fetchPage() throws ScraperHttpException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(pageUri))
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
}
