package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.model.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ctzn.youtubescraper.http.IoUtil.*;

@Log
public class YoutubeHttpClient {

    private final static String VIDEO_PAGE_URI_TEMPLATE = "https://www.youtube.com/watch?v=%s";
    private static final int REQUEST_URI_LENGTH_LIMIT = 14000;
    private final String videoId;

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0";
    private final static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    private final static String ACCEPT_ALL = "*/*";
    private final static String ACCEPT_LANGUAGE = "en-US,en;q=0.5";
    private final static String ACCEPT_ENCODING = "gzip, deflate, br";

    private final String videoPageUri;
    private final HttpClient httpClient;
    private final CustomCookieManager cookies;
    private final YoutubeConfigDTO youtubeConfig;
    private CommentItemSection commentItemSection;

    private Counter commentCounter = new Counter("Processed comments");
    private Counter replyCounter = new Counter("Processed replies");

    // value is set in constructor after the video page is parsed and updated after each
    private String currentXsrfToken;

    // values are set after the first comment section which contains a comment thread header is fetched
    private SectionHeaderDTO commentThreadHeader;
    private int renderCommentCounter;
    private Counter renderReplyCounter;
    private int currentUriLength;

    private final CommentApiRequestUriFactory commentApiRequestUriFactory = new CommentApiRequestUriFactory();
    private final CommentApiResponseParser commentApiResponseParser = new CommentApiResponseParser();

    private final CommentHandler[] handlers;

    public YoutubeHttpClient(String videoId, CommentHandler... handlers) throws Exception {
        this.videoId = videoId;
        this.handlers = handlers;
        videoPageUri = String.format(VIDEO_PAGE_URI_TEMPLATE, videoId);
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        cookies = new CustomCookieManager("youtube.com");
        log.info(() -> String.format("Fetch video page: [%s]", videoPageUri));
        String body = fetchVideoPage(videoPageUri);
        log.info(() -> "Scrape initial youtube context");
        youtubeConfig = VideoPageBodyParser.scrapeYoutubeConfig(body);
        currentXsrfToken = youtubeConfig.xsrfToken;
        log.info(() -> "Scrape comment section continuation");
        commentItemSection = VideoPageBodyParser.scrapeInitialCommentItemSection(body);
    }

    private String fetchVideoPage(String uri) throws IOException {
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

    public boolean hasContinuation() {
        return commentItemSection != null && commentItemSection.hasContinuation();
    }

    private <T extends ApiResponse> CommentItemSection requestContinuation(NextContinuationData continuationData, Class<T> valueType) throws Exception {
        URI requestUri = commentApiRequestUriFactory.newRequestUri(continuationData);
        currentUriLength = requestUri.toString().length();
        if (currentUriLength >= REQUEST_URI_LENGTH_LIMIT)
            throw new Exception("Request entity size limit is exceeded: no further processing of the continuation branch is possible");
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
//              .headers("X-YouTube-Ad-Signals")
                .headers("X-SPF-Referer", videoPageUri)
                .headers("X-SPF-Previous", videoPageUri)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Cookie", cookies.getHeader())
                .headers("Origin", "https://www.youtube.com")
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .POST(ofFormData(Map.of(youtubeConfig.xsrfFieldName, currentXsrfToken))).build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));
        T commentApiResponse = commentApiResponseParser.parseResponseBody(body, valueType);

        currentXsrfToken = commentApiResponse.getToken();

        return commentApiResponse.getItemSection();
    }

    public YoutubeHttpClient nextContinuation() {
        if (!hasContinuation()) return this;

        try {
            commentItemSection = requestContinuation(commentItemSection.nextContinuation(), CommentApiResponse.class);
        } catch (Exception e) {
            log.warning(e.getMessage());
            commentItemSection = null;
            return this;
        }

        if (commentItemSection.hasHeader()) {
            commentThreadHeader = commentItemSection.getHeader();
            renderCommentCounter = commentApiResponseParser.parseCommentsCountText(commentThreadHeader.commentsCountText);
            log.info(() -> "Total comments count: " + renderCommentCounter);
        }

        commentCounter.addAll(commentItemSection.countComments(), 1, currentUriLength);
        renderReplyCounter = new Counter("Render reply counter", commentItemSection.sumReplyCounters(), commentItemSection.countReplyContinuations());

        logStat();

        List<CommentDTO> comments = commentItemSection.getComments(videoId, null);
        Map<String, NextContinuationData> replyContinuationsMap = commentItemSection.getReplyContinuationsMap();

        for (CommentDTO comment : comments) {
            Arrays.stream(handlers).forEach(handler -> handler.handle(List.of(comment)));
            NextContinuationData c = replyContinuationsMap.get(comment.commentId);
            if (c != null) fetchReplies(Map.of(comment.commentId, c));
        }

        return this;
    }

    private void logStat() {
        Counter c = Counter.sum("", commentCounter, replyCounter);
        double commentRequestLimit = commentCounter.getUriLength() * 100f / REQUEST_URI_LENGTH_LIMIT;
        double replyRequestLimit = replyCounter.getUriLength() * 100f / REQUEST_URI_LENGTH_LIMIT;
        double done = c.getCounter() * 100f / renderCommentCounter;
        String s = String.format("%s continuations (com/rep): %s/%s, limits com/rep: %.0f%%/%.0f%%, comments count/done: %s/%.1f%%", videoId,
                commentCounter.getContinuationCounter(), replyCounter.getContinuationCounter(),
                commentRequestLimit, replyRequestLimit,
                c.getCounter(), done);
        log.fine(s);
    }

    private void fetchReplies(Map<String, NextContinuationData> replyContinuationsMap) {
        replyContinuationsMap.forEach((commentId, replyContinuation) -> {
            do {
                try {
                    CommentItemSection replyItemSection = requestContinuation(replyContinuation, ReplyApiResponse.class);
                    if (replyItemSection == null) return;
                    List<CommentDTO> replies = replyItemSection.getComments(videoId, commentId);
                    Arrays.stream(handlers).forEach(handler -> handler.handle(replies));
                    replyContinuation = replyItemSection.hasContinuation() ? replyItemSection.nextContinuation() : null;
                    replyCounter.addAll(replyItemSection.countComments(), 1, currentUriLength);
                    logStat();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.warning(e.getMessage());
                    return;
                }
            } while (replyContinuation != null);
        });
    }

    public Counter getCommentCounter() {
        return commentCounter;
    }

    public Counter getReplyCounter() {
        return replyCounter;
    }

    public Counter getRenderReplyCounter() {
        return renderReplyCounter;
    }

    public int getRenderCommentCounter() {
        return renderCommentCounter;
    }
}
