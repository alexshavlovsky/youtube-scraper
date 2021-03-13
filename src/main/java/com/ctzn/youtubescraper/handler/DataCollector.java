package com.ctzn.youtubescraper.handler;

import java.util.ArrayList;
import java.util.List;

// WARNING: this class is not thread-safe

public class DataCollector<T> extends ArrayList<T> implements DataHandler<T> {

    @Override
    public void accept(List<T> ts) {
        addAll(ts);
    }

}
