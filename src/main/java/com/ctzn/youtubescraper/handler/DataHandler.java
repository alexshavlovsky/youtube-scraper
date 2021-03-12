package com.ctzn.youtubescraper.handler;

import java.util.List;

@FunctionalInterface
public interface DataHandler<T> {
    void handle(List<T> items);
}
