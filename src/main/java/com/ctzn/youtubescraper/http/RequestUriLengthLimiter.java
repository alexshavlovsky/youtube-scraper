package com.ctzn.youtubescraper.http;

public interface RequestUriLengthLimiter {
    int getUriLength();

    void setUriLength(int uriLength);

    double getUriLengthLimitUsagePercent();
}
