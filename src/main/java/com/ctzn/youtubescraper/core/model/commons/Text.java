package com.ctzn.youtubescraper.core.model.commons;

import java.util.List;
import java.util.stream.Collectors;

public class Text {
    public List<Run> runs;

    @Override
    public String toString() {
        return runs == null ? "" : runs.stream().map(r -> r.text).collect(Collectors.joining());
    }
}
