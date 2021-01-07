package service;

import parser.VideoPageBodyParser;

import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

import static service.IoUtil.*;

public class CommentApiHttpClient extends BaseHttpClient {

    private final static String COMMENT_API_URL = "https://www.youtube.com/comment_service_ajax?action_get_comments=1&pbj=1&";

    public static HttpContext fetchComments(VideoPageHttpClient.HttpContext context, VideoPageBodyParser.VideoPageSession session) {

        HttpRequest request = HttpRequest.newBuilder(URI.create(COMMENT_API_URL + joinQueryParams(Map.of(
                "ctoken", session.continuation,
                "continuation", session.continuation,
                "itct", session.itct
        ))))
                .headers("User-Agent", USER_AGENT)
                .headers("Accept", ACCEPT_ALL)
                .headers("Accept-Language", ACCEPT_LANGUAGE)
                .headers("Accept-Encoding", ACCEPT_ENCODING)
                .headers("Referer", context.referer)
                .headers("X-YouTube-Client-Name", "1")
                .headers("X-YouTube-Client-Version", "2.20201220.08.00")
                .headers("X-YouTube-Device", "cbr=Firefox&cbrver=84.0&ceng=Gecko&cengver=84.0&cos=Windows&cosver=6.1&cplatform=DESKTOP")
                .headers("X-YouTube-Page-Label", "youtube.desktop.web_20201220_08_RC00")
                .headers("X-YouTube-Utc-Offset", "120")
                .headers("X-YouTube-Time-Zone", "Europe/Kaliningrad")
                .headers("X-SPF-Referer", context.referer)
                .headers("X-SPF-Previous", context.referer)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Origin", "https://www.youtube.com")
                .headers("DNT", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .headers("TE", "Trailers")
                .POST(ofFormData(Map.of(session.xsrfFieldName, session.xsrfToken))).build();

        context.cookies.getCookieStore().add(URI.create("http://www.youtube.com"), HttpCookie.parse("PREF=f4=4000000").get(0));
        HttpResponse<InputStream> httpResponse = fetch(context.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));
        HttpHeaders headers = httpResponse.headers();
        InputStream body = httpResponse.body();

        Optional<String> contentEncoding = headers.firstValue("content-encoding");
        if (contentEncoding.isPresent() && "br".equals(contentEncoding.get())) body = applyBrotliDecoder(body);

        return new HttpContext(context.httpClient, headers, body, context.cookies, context.referer);
    }

}
