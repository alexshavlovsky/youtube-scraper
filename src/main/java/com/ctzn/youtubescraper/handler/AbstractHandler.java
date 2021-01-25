package com.ctzn.youtubescraper.handler;

import java.util.List;

@FunctionalInterface
public interface AbstractHandler<T> {
    void handle(List<T> items);
}
