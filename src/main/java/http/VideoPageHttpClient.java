package http;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static http.IoUtil.*;

public class VideoPageHttpClient extends AbstractHttpClient {

    public static HttpContext fetchVideoPage(String videoId) {

        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

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

        HttpResponse<InputStream> httpResponse = complete(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));

        CustomCookieManager cookies = new CustomCookieManager("youtube.com");
        cookies.put(httpResponse.headers());

        String body = readStreamToString(applyBrotliDecoderAndGetBody(httpResponse));

        return new HttpContext(httpClient, body, cookies, referer);
    }
}