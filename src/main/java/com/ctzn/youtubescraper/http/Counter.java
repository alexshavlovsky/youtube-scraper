package com.ctzn.youtubescraper.http;

public class Counter {
    private int counter = 0;
    private int continuationCounter = 0;

    void addAll(int counterValue, int continuationCounterValue) {
        counter += counterValue;
        continuationCounter += continuationCounterValue;
    }

    void add(int value) {
        counter += value;
    }

    void incContinuation() {
        continuationCounter++;
    }

    void addContinuations(int value) {
        continuationCounter += value;
    }

    public int getCounter() {
        return counter;
    }

    public int getContinuationCounter() {
        return continuationCounter;
    }
}
