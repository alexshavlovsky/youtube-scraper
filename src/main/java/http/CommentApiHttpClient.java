package http;

import parser.VideoPageBodyParser;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

import static http.IoUtil.*;

public class CommentApiHttpClient extends BaseHttpClient {

    private final static String COMMENT_API_URL = "https://www.youtube.com/comment_service_ajax?";

    public static HttpContext fetchComments(VideoPageHttpClient.HttpContext context, VideoPageBodyParser.VideoPageSession session) {

        Map<String, String> queryParams = new LinkedHashMap<>();
        queryParams.put("action_get_comments", "1");
        queryParams.put("pbj", "1");
        queryParams.put("ctoken", session.continuation);
        queryParams.put("continuation", session.continuation);
        queryParams.put("itct", session.itct);

        context.cookies.put("PREF=f4=4000000");

        HttpRequest request = HttpRequest.newBuilder(URI.create(COMMENT_API_URL + joinQueryParams(queryParams)))
                .headers("User-Agent", USER_AGENT)
                .headers("Accept", ACCEPT_ALL)
                .headers("Accept-Language", ACCEPT_LANGUAGE)
                .headers("Accept-Encoding", ACCEPT_ENCODING)
                .headers("Referer", context.referer)
                .headers("X-YouTube-Client-Name", session.ytCfg.INNERTUBE_CONTEXT_CLIENT_NAME)
                .headers("X-YouTube-Client-Version", session.ytCfg.INNERTUBE_CONTEXT_CLIENT_VERSION)
                .headers("X-YouTube-Device", session.ytCfg.DEVICE)
                .headers("X-YouTube-Page-CL", session.ytCfg.PAGE_CL)
                .headers("X-YouTube-Page-Label", session.ytCfg.PAGE_BUILD_LABEL)
                .headers("X-YouTube-Utc-Offset", "180")
                .headers("X-YouTube-Time-Zone", "Europe/Minsk")
//                .headers("X-YouTube-Ad-Signals")
                .headers("X-SPF-Referer", context.referer)
                .headers("X-SPF-Previous", context.referer)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Cookie", context.cookies.getHeader())
                .headers("Origin", "https://www.youtube.com")
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .POST(ofFormData(Map.of(session.ytCfg.XSRF_FIELD_NAME, session.ytCfg.XSRF_TOKEN))).build();

        HttpResponse<InputStream> httpResponse = fetch(context.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));

        String body = readStreamToString(applyBrotliDecoderAndGetBody(httpResponse));

        return new HttpContext(context.httpClient, body, context.cookies, context.referer);
    }
}
