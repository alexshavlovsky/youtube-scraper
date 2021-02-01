package com.ctzn.youtubescraper.iterator.video;

class VideoContextMeter {
    private int counter;
    private int continuationCounter;

    VideoContextMeter() {
    }

    void add(int value) {
        counter += value;
    }

    void add(VideoContextMeter meter) {
        counter += meter.counter;
        continuationCounter += meter.continuationCounter;
    }

    void update(int count) {
        incContinuation();
        add(count);
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

    void reset() {
        counter = 0;
        continuationCounter = 0;
    }

    @Override
    public String toString() {
        return String.format("cont: %s, counter: %s", getContinuationCounter(), getCounter());
    }
}
