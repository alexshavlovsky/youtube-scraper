package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.model.CommentApiResponse;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.SectionHeaderDTO;
import com.ctzn.youtubescraper.model.YoutubeConfigDTO;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.ctzn.youtubescraper.http.IoUtil.*;

@Log
public class YoutubeHttpClient {

    private final static String VIDEO_PAGE_URI_TEMPLATE = "https://www.youtube.com/watch?v=%s";

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0";
    private final static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    private final static String ACCEPT_ALL = "*/*";
    private final static String ACCEPT_LANGUAGE = "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3";
    private final static String ACCEPT_ENCODING = "gzip, deflate, br";

    private final String videoPageUri;
    private final HttpClient httpClient;
    private final CustomCookieManager cookies;
    private final YoutubeConfigDTO youtubeConfig;
    private CommentItemSection commentItemSection;

    private Counter commentCounter = new Counter();
    private Counter replyCounter = new Counter();

    // value is set in constructor after the video page is parsed and updated after each
    private String currentXsrfToken;

    // values are set after the first comment section which contains a comment thread header is fetched
    private SectionHeaderDTO commentThreadHeader;
    private int headerCommentCounter;

    private final CommentApiRequestUriFactory commentApiRequestUriFactory = new CommentApiRequestUriFactory();
    private final CommentApiResponseParser commentApiResponseParser = new CommentApiResponseParser();

    public YoutubeHttpClient(String videoId) {
        videoPageUri = String.format(VIDEO_PAGE_URI_TEMPLATE, videoId);
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        cookies = new CustomCookieManager("youtube.com");
        log.info(() -> "Fetch video page: " + videoPageUri);
        String body = fetchVideoPage(videoPageUri);
        log.info(() -> "Scrape initial youtube context");
        youtubeConfig = VideoPageBodyParser.scrapeYoutubeConfig(body);
        currentXsrfToken = youtubeConfig.xsrfToken;
        log.info(youtubeConfig::toString);
        commentItemSection = VideoPageBodyParser.scrapeInitialCommentItemSection(body);
        log.fine(() -> commentItemSection.nextContinuation().toString());
    }

    private String fetchVideoPage(String uri) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
                .headers("User-Agent", USER_AGENT)
                .headers("Accept", ACCEPT)
                .headers("Accept-Language", ACCEPT_LANGUAGE)
                .headers("Accept-Encoding", ACCEPT_ENCODING)
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

    public boolean hasComments() {
        return commentItemSection.hasContinuation();
    }

    // TODO parametrize this using ApiResponse interface and update the token inside the method
    // Warning! XSRF token must be updated by the caller itself
    private HttpResponse<InputStream> requestContinuation(NextContinuationData continuationData) {
        URI requestUri = commentApiRequestUriFactory.newRequestUri(continuationData);
        HttpRequest request = HttpRequest.newBuilder(requestUri)
                .headers("User-Agent", USER_AGENT)
                .headers("Accept", ACCEPT_ALL)
                .headers("Accept-Language", ACCEPT_LANGUAGE)
                .headers("Accept-Encoding", ACCEPT_ENCODING)
                .headers("Referer", videoPageUri)
                .headers("X-YouTube-Client-Name", youtubeConfig.clientName)
                .headers("X-YouTube-Client-Version", youtubeConfig.clientVersion)
                .headers("X-YouTube-Device", youtubeConfig.device)
                .headers("X-YouTube-Page-CL", youtubeConfig.pageCl)
                .headers("X-YouTube-Page-Label", youtubeConfig.pageLabel)
                .headers("X-YouTube-Utc-Offset", "180")
                .headers("X-YouTube-Time-Zone", "Europe/Minsk")
//                .headers("X-YouTube-Ad-Signals")
                .headers("X-SPF-Referer", videoPageUri)
                .headers("X-SPF-Previous", videoPageUri)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Cookie", cookies.getHeader())
                .headers("Origin", "https://www.youtube.com")
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .POST(ofFormData(Map.of(youtubeConfig.xsrfFieldName, currentXsrfToken))).build();
        return completeRequest(httpClient, request);
    }

    public CommentItemSection nextComments() {
        if (!hasComments()) return null;

        HttpResponse<InputStream> httpResponse = requestContinuation(commentItemSection.nextContinuation());

        String responseBody = readStreamToString(applyBrotliDecoderAndGetBody(httpResponse));
        CommentApiResponse commentApiResponse = commentApiResponseParser.parseResponseBody(responseBody);

        commentItemSection = commentApiResponse.getItemSection();
        currentXsrfToken = commentApiResponse.xsrf_token;

        if (commentItemSection.hasHeader()) {
            commentThreadHeader = commentItemSection.getHeader();
            headerCommentCounter = commentApiResponseParser.parseCommentsCountText(commentThreadHeader.commentsCountText);
            log.info(() -> "Total comments count: " + headerCommentCounter);
        }

        commentCounter.addAll(commentItemSection.countComments(), 1);
        log.fine(() -> "Continuations processed: " + commentCounter.getContinuationCounter());
        log.fine(() -> "Comments processed: " + commentCounter.getCounter());

        replyCounter.addAll(commentItemSection.sumReplyCounters(), commentItemSection.countReplyContinuations());
        log.fine(() -> "Reply continuations processed: " + replyCounter.getContinuationCounter());
        log.fine(() -> "Replies processed: " + replyCounter.getCounter());

        fetchReplies(commentItemSection.getReplyContinuationsMap());

        return commentItemSection;
    }

    private void fetchReplies(Map<String, NextContinuationData> replyContinuationsMap) {
        replyContinuationsMap.forEach((key, value) -> {
            System.out.println(key);
//            HttpResponse<InputStream> is = requestContinuation(value);
//            String responseBody = readStreamToString(applyBrotliDecoderAndGetBody(is));
//            try {
//                Files.writeString(Path.of(key + ".json"), responseBody);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        });
    }

    public Counter getCommentCounter() {
        return commentCounter;
    }

    public Counter getReplyCounter() {
        return replyCounter;
    }
}