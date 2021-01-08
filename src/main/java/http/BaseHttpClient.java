package http;

import java.net.http.HttpClient;

public class BaseHttpClient {
    public static class HttpContext {
        public final HttpClient httpClient;
        public final String body;
        public final CustomCookieManager cookies;
        public final String referer;

        HttpContext(HttpClient httpClient, String body, CustomCookieManager cookies, String referer) {
            this.httpClient = httpClient;
            this.body = body;
            this.cookies = cookies;
            this.referer = referer;
        }
    }

    final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0";
    final static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    final static String ACCEPT_ALL = "*/*";
    final static String ACCEPT_LANGUAGE = "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3";
    final static String ACCEPT_ENCODING = "gzip, deflate, br";
}
