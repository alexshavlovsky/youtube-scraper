package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import org.brotli.dec.BrotliInputStream;

import java.io.*;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

class IoUtil {

    static private InputStream applyBrotliDecoder(InputStream inputStream) throws ScraperHttpException {
        try {
            return new BrotliInputStream(inputStream);
        } catch (IOException e) {
            throw new ScraperHttpException(e.getMessage(), e);
        }
    }

    static InputStream applyBrotliDecoderAndGetBody(HttpResponse<InputStream> response) throws ScraperHttpException {
        Optional<String> contentEncoding = response.headers().firstValue("content-encoding");
        return contentEncoding.isPresent() && "br".equals(contentEncoding.get()) ? applyBrotliDecoder(response.body()) : response.body();
    }

    static void transferInputStreamToFile(InputStream input, File file) throws IOException {
        try (OutputStream output = new FileOutputStream(file, false)) {
            input.transferTo(output);
        }
    }

    private static <T> HttpResponse<T> complete(CompletableFuture<HttpResponse<T>> future) throws ScraperHttpException, ScrapperInterruptedException {
        try {
            if (!Thread.currentThread().isInterrupted()) return future.get();
        } catch (ExecutionException e) {
            throw new ScraperHttpException(e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new ScrapperInterruptedException("Http request has been interrupted");
    }

    static HttpResponse<InputStream> completeRequest(HttpClient httpClient, HttpRequest request) throws ScraperHttpException, ScrapperInterruptedException {
        HttpResponse<InputStream> response = complete(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));
        if (response.statusCode() != 200)
            throw new ScraperHttpException("Status code [%s] was returned when requesting a resource at [%s]", response.statusCode(), response.uri());
        return response;
    }

    private static String urlEncode(String source) {
        return URLEncoder.encode(source, StandardCharsets.UTF_8);
    }

    private static String joinQueryParams(Map<String, String> data) {
        return data.entrySet().stream().map(e -> String.join("=", e.getKey(), urlEncode(e.getValue()))).collect(Collectors.joining("&"));
    }

    static String joinQueryParamsOrdered(String... params) {
        if (params.length % 2 != 0)
            throw new IllegalArgumentException("Invalid parameters passed to method: static String joinQueryParamsOrdered(String... params)");
        int pos = 0;
        Map<String, String> queryParams = new LinkedHashMap<>();
        while (pos < params.length) queryParams.put(params[pos++], params[pos++]);
        return joinQueryParams(queryParams);
    }

    static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        return HttpRequest.BodyPublishers.ofString(joinQueryParams(data));
    }

    static String readStreamToString(InputStream inputStream) throws ScraperHttpException {
        try {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ScraperHttpException(e.getMessage(), e);
        }
    }
}
