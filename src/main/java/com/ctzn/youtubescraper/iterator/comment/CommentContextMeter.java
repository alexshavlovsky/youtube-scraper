package com.ctzn.youtubescraper.iterator.comment;

import com.ctzn.youtubescraper.http.RequestUriLengthLimiter;
import com.ctzn.youtubescraper.iterator.CompletionPredictor;

class CommentContextMeter implements RequestUriLengthLimiter {
    private final static int REQUEST_URI_LENGTH_LIMIT = 14000;
    private int counter;
    private int continuationCounter;
    private int targetCount;
    private int uriLength;
    private CompletionPredictor completionPredictor = new CompletionPredictor(100);

    void update(int count) {
        continuationCounter++;
        counter += count;
        runPredictor();
    }

    private void runPredictor() {
        if (targetCount != 0) completionPredictor.predict(getCompletionPercent());
    }

    int getCounter() {
        return counter;
    }

    int getContinuationCounter() {
        return continuationCounter;
    }

    int getTargetCount() {
        return targetCount;
    }

    void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    double getCompletionPercent() {
        return percent(counter, targetCount);
    }

    String formatCompletionString() {
        if (targetCount == 0) return "";
        runPredictor();
        return String.format(" (%s%.1f%%)", completionPredictor.format(), getCompletionPercent());
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
        targetCount = 0;
        uriLength = 0;
    }

    @Override
    public String toString() {
        return String.format("cont/limit: %s/%.0f%%, counter/completion: %s/%.1f%%",
                continuationCounter, getUriLengthLimitUsagePercent(),
                counter, getCompletionPercent());
    }
}
