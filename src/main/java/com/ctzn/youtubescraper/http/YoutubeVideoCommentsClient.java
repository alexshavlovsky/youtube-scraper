package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.model.comments.ApiResponse;
import com.ctzn.youtubescraper.model.comments.CommentApiResponse;
import com.ctzn.youtubescraper.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.model.comments.ReplyApiResponse;
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

    public YoutubeVideoCommentsClient(UserAgentFactory userAgentFactory, String videoId) throws ScraperParserException, ScraperHttpException, ScrapperInterruptedException {
        super(userAgentFactory, uriFactory.newVideoPageUri(videoId), videoPageBodyParser::scrapeInitialCommentItemSection);
        this.videoId = videoId;
        if (!initialData.hasContinuation()) throw new ScraperParserException("Initial comment continuation not found");
    }

    public CommentItemSection requestNextCommentSection(NextContinuationData continuationData, RequestUriLengthLimiter limiter) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        URI requestUri = uriFactory.newCommentApiRequestUri(continuationData);
        return requestNextSection(requestUri, limiter, CommentApiResponse.class);
    }

    public CommentItemSection requestNextReplySection(NextContinuationData continuationData, RequestUriLengthLimiter limiter) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        URI requestUri = uriFactory.newReplyApiRequestUri(continuationData);
        return requestNextSection(requestUri, limiter, ReplyApiResponse.class);
    }

    private <T extends ApiResponse> CommentItemSection requestNextSection(URI requestUri, RequestUriLengthLimiter limiter, Class<T> valueType) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        limiter.setUriLength(requestUri.toString().length());
        if (limiter.getUriLengthLimitUsagePercent() > 100)
            throw new ScraperHttpException("Request entity size limit is exceeded: no further processing of the section is possible");

        HttpRequest request = newApiRequestBuilder(requestUri)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Origin", "https://www.youtube.com")
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
