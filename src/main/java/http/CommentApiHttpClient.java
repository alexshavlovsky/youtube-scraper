package http;

import model.ContinuationData;
import model.YoutubeConfig;
import parser.VideoPageBodyParser;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static http.IoUtil.*;

public class CommentApiHttpClient extends AbstractHttpClient {

    private final static String COMMENT_API_URL_TEMPLATE = "https://www.youtube.com/comment_service_ajax?%s";

    public static HttpContext fetchComments(VideoPageHttpClient.HttpContext httpContext, VideoPageBodyParser.YoutubeContext youtubeContext) {

        ContinuationData continuationData = youtubeContext.commentItemSection.getContinuationData();
        YoutubeConfig youtubeConfig = youtubeContext.youtubeConfig;

        String queryParams = joinQueryParamsOrdered(
                "action_get_comments", "1",
                "pbj", "1",
                "ctoken", continuationData.continuation,
                "continuation", continuationData.continuation,
                "itct", continuationData.clickTrackingParams
        );

        httpContext.cookies.put("PREF=f4=4000000");

        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format(COMMENT_API_URL_TEMPLATE, queryParams)))
                .headers("User-Agent", USER_AGENT)
                .headers("Accept", ACCEPT_ALL)
                .headers("Accept-Language", ACCEPT_LANGUAGE)
                .headers("Accept-Encoding", ACCEPT_ENCODING)
                .headers("Referer", httpContext.referer)
                .headers("X-YouTube-Client-Name", youtubeConfig.INNERTUBE_CONTEXT_CLIENT_NAME)
                .headers("X-YouTube-Client-Version", youtubeConfig.INNERTUBE_CONTEXT_CLIENT_VERSION)
                .headers("X-YouTube-Device", youtubeConfig.DEVICE)
                .headers("X-YouTube-Page-CL", youtubeConfig.PAGE_CL)
                .headers("X-YouTube-Page-Label", youtubeConfig.PAGE_BUILD_LABEL)
                .headers("X-YouTube-Utc-Offset", "180")
                .headers("X-YouTube-Time-Zone", "Europe/Minsk")
//                .headers("X-YouTube-Ad-Signals")
                .headers("X-SPF-Referer", httpContext.referer)
                .headers("X-SPF-Previous", httpContext.referer)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Cookie", httpContext.cookies.getHeader())
                .headers("Origin", "https://www.youtube.com")
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .POST(ofFormData(Map.of(youtubeConfig.XSRF_FIELD_NAME, youtubeConfig.XSRF_TOKEN))).build();

        HttpResponse<InputStream> httpResponse = complete(httpContext.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));

        String body = readStreamToString(applyBrotliDecoderAndGetBody(httpResponse));

        return new HttpContext(httpContext.httpClient, body, httpContext.cookies, httpContext.referer);
    }
}
