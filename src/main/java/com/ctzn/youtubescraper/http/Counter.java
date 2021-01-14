package com.ctzn.youtubescraper.http;

public class Counter {
    private String name;
    private int counter;
    private int continuationCounter;
    private int uriLength;

    public Counter(String name, int counter, int continuationCounter) {
        this.name = name;
        this.counter = counter;
        this.continuationCounter = continuationCounter;
    }

    public Counter(String name) {
        this.name = name;
    }

    static Counter sum(String name, Counter... counters) {
        int c = 0, cc = 0;
        for (Counter counter : counters) {
            c += counter.counter;
            cc += counter.continuationCounter;
        }
        return new Counter(name, c, cc);
    }

    void addAll(int counterValue, int continuationCounterValue, int uriLength) {
        counter += counterValue;
        continuationCounter += continuationCounterValue;
        this.uriLength = uriLength;
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

    public int getUriLength() {
        return uriLength;
    }

    @Override
    public String toString() {
        return name + ": " + counter + " in " + continuationCounter + " continuations";
    }
}
