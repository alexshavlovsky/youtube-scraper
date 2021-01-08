package service;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class CustomCookieManager extends CookieManager {

    private final String defaultDomain;
    private final URI defaultUri;

    CustomCookieManager(String defaultDomain) {
        this.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.defaultDomain = defaultDomain;
        this.defaultUri = URI.create("www." + defaultDomain);
    }

    private void put(URI uri, HttpHeaders headers) {
        try {
            put(uri, headers.map());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void put(HttpHeaders headers) {
        put(defaultUri, headers);
    }

    private static final String COOKIE_TEMPLATE = "%s; path=/; domain=.%s";

    void put(String cookie) {
        try {
            put(defaultUri, Map.of("Set-Cookie", List.of(String.format(COOKIE_TEMPLATE, cookie, defaultDomain))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String getHeader() {
        return getCookieStore().getCookies().stream().map(HttpCookie::toString).collect(Collectors.joining("; "));
    }
}
