package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.http.RequestUriLengthLimiter;

class CommentContextMeter implements RequestUriLengthLimiter {
    private final static int REQUEST_URI_LENGTH_LIMIT = 14000;
    private int counter;
    private int continuationCounter;
    private int targetCounter;
    private int uriLength;

    CommentContextMeter() {
    }

    void add(int value) {
        counter += value;
    }

    void add(CommentContextMeter meter) {
        counter += meter.counter;
        continuationCounter += meter.continuationCounter;
    }

    void incContinuation() {
        continuationCounter++;
    }

    int getCounter() {
        return counter;
    }

    int getContinuationCounter() {
        return continuationCounter;
    }

    int getTargetCounter() {
        return targetCounter;
    }

    void setTargetCounter(int targetCounter) {
        this.targetCounter = targetCounter;
    }

    double getCompletionPercent() {
        return percent(counter, targetCounter);
    }

    @Override
    public int getUriLength() {
        return uriLength;
    }

    @Override
    public void setUriLength(int uriLength) {
        this.uriLength = uriLength;
    }

    @Override
    public double getUriLengthLimitUsagePercent() {
        return percent(uriLength, REQUEST_URI_LENGTH_LIMIT);
    }

    private static double percent(int actual, int target) {
        return actual * 100f / target;
    }

    void reset() {
        counter = 0;
        continuationCounter = 0;
        targetCounter = 0;
        uriLength = 0;
    }

    @Override
    public String toString() {
        return String.format("cont/limit: %s/%.0f%%, counter/completion: %s/%.1f%%",
                getContinuationCounter(), getUriLengthLimitUsagePercent(),
                getCounter(), getCompletionPercent());
    }
}
