package com.ctzn.youtubescraper.handler;

import java.util.ArrayList;
import java.util.List;

public class DataCollector<T> extends ArrayList<T> implements DataHandler<T> {

    @Override
    public void handle(List<T> items) {
        addAll(items);
    }

}
