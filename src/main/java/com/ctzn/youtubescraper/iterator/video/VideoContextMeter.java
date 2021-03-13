package com.ctzn.youtubescraper.iterator.video;

class VideoContextMeter {
    private int counter;
    private int continuationCounter;

    void update(int count) {
        continuationCounter++;
        counter += count;
    }

    int getCounter() {
        return counter;
    }

    int getContinuationCounter() {
        return continuationCounter;
    }

    @Override
    public String toString() {
        return String.format("cont: %s, counter: %s", continuationCounter, counter);
    }
}
