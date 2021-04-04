package com.ctzn.youtubescraper.core.model.commons;

public class SimpleText {
    public String simpleText;

    @Override
    public String toString() {
        return simpleText == null ? "" : simpleText;
    }
}
