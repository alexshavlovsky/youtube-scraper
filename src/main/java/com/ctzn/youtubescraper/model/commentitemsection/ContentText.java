package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

import java.util.List;

@Value
class ContentText {
    public List<Run> runs;
}
