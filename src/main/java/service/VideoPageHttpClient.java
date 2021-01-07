package service;

import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static service.IoUtil.applyBrotliDecoder;
import static service.IoUtil.fetch;

public class VideoPageHttpClient extends BaseHttpClient {

    public static HttpContext fetchVideoPage(String videoId) {
        CookieManager cookies = new CookieManager();
        cookies.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).cookieHandler(cookies).build();

        String referer = String.format("https://www.youtube.com/watch?v=%s", videoId);

        HttpRequest request = HttpRequest.newBuilder(URI.create(referer))
                .headers("User-Agent", USER_AGENT)
                .headers("Accept", ACCEPT)
                .headers("Accept-Language", ACCEPT_LANGUAGE)
                .headers("Accept-Encoding", ACCEPT_ENCODING)
                .headers("DNT", "1")
                .headers("Upgrade-Insecure-Requests", "1")
                .headers("Pragma", "no-cache")
                .headers("Cache-Control", "no-cache")
                .GET().build();

        HttpResponse<InputStream> httpResponse = fetch(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));
        HttpHeaders headers = httpResponse.headers();
        InputStream body = httpResponse.body();

        Optional<String> contentEncoding = headers.firstValue("content-encoding");
        if (contentEncoding.isPresent() && "br".equals(contentEncoding.get())) body = applyBrotliDecoder(body);

        return new HttpContext(httpClient, headers, body, cookies, referer);
    }
}
