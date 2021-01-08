package service;

import org.brotli.dec.BrotliInputStream;

import java.io.*;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class IoUtil {

    static private InputStream applyBrotliDecoder(InputStream inputStream) {
        try {
            return new BrotliInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static InputStream applyBrotliDecoderAndGetBody(HttpResponse<InputStream> response) {
        Optional<String> contentEncoding = response.headers().firstValue("content-encoding");
        return contentEncoding.isPresent() && "br".equals(contentEncoding.get()) ? applyBrotliDecoder(response.body()) : response.body();
    }

    public static void transferInputStreamToFile(InputStream input, File file) {
        try (OutputStream output = new FileOutputStream(file, false)) {
            input.transferTo(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static HttpResponse<InputStream> fetch(CompletableFuture<HttpResponse<InputStream>> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private static String urlEncode(String source) {
        return URLEncoder.encode(source, StandardCharsets.UTF_8);
    }

    static String joinQueryParams(Map<String, String> data) {
        return data.entrySet().stream().map(e -> String.join("=", e.getKey(), urlEncode(e.getValue()))).collect(Collectors.joining("&"));
    }

    static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        return HttpRequest.BodyPublishers.ofString(joinQueryParams(data));
    }

    static String readStreamToString(InputStream inputStream) {
        try {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
