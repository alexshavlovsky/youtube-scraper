package com.ctzn.youtubescraper.config;

public class CountLimit {

    private boolean isUnrestricted = true;
    private int limit = 0;

    public void set(int value) {
        limit = value;
        isUnrestricted = false;
    }

    public void reset() {
        isUnrestricted = true;
    }

    public int get() {
        if (isUnrestricted) throw new IllegalStateException("Unrestricted limit has no value");
        return limit;
    }

    public boolean isValueBelowLimit(int value) {
        return isUnrestricted || value < limit;
    }

    public boolean isLimitedToZero() {
        return !isUnrestricted && limit == 0;
    }

    public boolean isUnrestricted() {
        return isUnrestricted;
    }

}
