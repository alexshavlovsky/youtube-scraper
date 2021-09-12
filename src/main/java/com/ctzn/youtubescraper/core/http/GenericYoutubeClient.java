package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentCfg;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.core.model.YoutubeCfgDTO;
import com.ctzn.youtubescraper.core.parser.VideoPageBodyParser;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.ctzn.youtubescraper.core.http.IoUtil.*;

@Log
class GenericYoutubeClient<E> {
    static final YoutubeUriFactory uriFactory = new YoutubeUriFactory();
    static final VideoPageBodyParser videoPageBodyParser = new VideoPageBodyParser();
    final static HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    final CustomCookieManager cookies = new CustomCookieManager("youtube.com");
    private final UserAgentCfg userAgentCfg;
    final String pageUri;
    final E initialData;
    String currentXsrfToken;
    final String youtubeCfgJson;
    final YoutubeCfgDTO youtubeCfg;

    GenericYoutubeClient(UserAgentFactory userAgentFactory, String pageUri, YoutubeInitialDataHandler<E> youtubeInitialDataHandler) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        this.userAgentCfg = userAgentFactory.getUserAgentCfg();
        this.pageUri = pageUri;
        log.info(String.format("Fetch page: [%s]", pageUri));
        String body = fetchPage();
        log.fine(() -> "Scrape initial youtube config");
        youtubeCfgJson = videoPageBodyParser.scrapeYoutubeConfigJson(body);
        youtubeCfg = videoPageBodyParser.parseYoutubeConfig(youtubeCfgJson);
        currentXsrfToken = youtubeCfg.getXsrfToken();
        if (currentXsrfToken == null) throw new ScraperParserException("Initial XSRF token not found");
        log.fine(() -> "Scrape initial youtube data");
        String ytInitialDataJson = videoPageBodyParser.scrapeYtInitialDataJson(body);
        log.fine(() -> "Parse  initial youtube data");
        initialData = youtubeInitialDataHandler.handle(ytInitialDataJson);
    }

    HttpRequest.Builder newRequestBuilder(URI requestUri, String acceptHeader) {
        return HttpRequest.newBuilder(requestUri).timeout(Duration.ofSeconds(10))
                .headers("User-Agent", userAgentCfg.getUserAgent())
                .headers("Accept", acceptHeader)
                .headers("Accept-Language", userAgentCfg.getAcceptLanguage())
                .headers("Accept-Encoding", userAgentCfg.getAcceptEncoding())
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache");
    }

    HttpRequest.Builder newApiRequestBuilder(URI requestUri) {
        return newRequestBuilder(requestUri, "*/*")
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
                .headers("Cookie", cookies.getHeader());
    }

    private String fetchPage() throws ScraperHttpException, ScrapperInterruptedException {
        HttpRequest request = newRequestBuilder(URI.create(pageUri), userAgentCfg.getAccept())
                .headers("Upgrade-Insecure-Requests", "1")
                .GET().build();

        HttpResponse<InputStream> httpResponse = completeRequest(httpClient, request);

        cookies.put(httpResponse.headers());
        cookies.put("PREF=f4=4000000");

        return readStreamToString(applyBrotliDecoderAndGetBody(httpResponse));
    }
}
