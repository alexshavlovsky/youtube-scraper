package com.ctzn.youtubescraper.iterator.video;

import com.ctzn.youtubescraper.model.channelvideos.VideoDTO;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

import java.util.List;

public interface IterableVideoContext {
    String getChannelId();

    boolean hasContinuation();

    NextContinuationData getContinuationData();

    void nextGrid(NextContinuationData continuationData);

    boolean hasGrid();

    VideosGrid getGrid();

    VideoContextMeter getMeter();

    String getShortResultStat();

    default List<VideoDTO> getVideos() {
        return getGrid().getVideos(getChannelId());
    }
}
