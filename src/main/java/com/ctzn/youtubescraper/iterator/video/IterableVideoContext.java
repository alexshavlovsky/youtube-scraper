package com.ctzn.youtubescraper.iterator.video;

import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

public interface IterableVideoContext {
    String getChannelId();

    boolean hasContinuation();

    NextContinuationData getContinuationData();

    void nextGrid(NextContinuationData continuationData);

    boolean hasGrid();

    VideosGrid getGrid();

    VideoContextMeter getMeter();

    String getShortResultStat();
}
