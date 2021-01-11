package com.ctzn.youtubescraper.http;

class Counter {
    private int counter = 0;
    private int continuationCounter = 0;

    void add(int value) {
        counter += value;
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
}
