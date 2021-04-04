package com.ctzn.youtubescraper.core.http;

public interface RequestUriLengthLimiter {
    int getUriLength();

    void setUriLength(int uriLength);

    double getUriLengthLimitUsagePercent();
}
