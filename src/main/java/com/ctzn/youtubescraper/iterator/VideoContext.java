package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.http.YoutubeChannelVideosClient;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

public class VideoContext implements IterableVideoContext {

    private final VideoContextMeter meter = new VideoContextMeter();
    private final YoutubeChannelVideosClient client;
    private VideosGrid grid;

    public VideoContext(YoutubeChannelVideosClient client) {
        this.client = client;
        this.grid = client.getInitialGrid();
    }

    @Override
    public String getChannelId() {
        return client.getChannelId();
    }

    @Override
    public boolean hasContinuation() {
        return grid.hasContinuation();
    }

    @Override
    public NextContinuationData getContinuationData() {
        return grid.getContinuation();
    }

    @Override
    public void nextGrid(NextContinuationData continuationData) {
        // TODO implement this
    }

    @Override
    public boolean hasGrid() {
        return grid != null;
    }

    @Override
    public VideosGrid getGrid() {
        return grid;
    }

    @Override
    public VideoContextMeter getMeter() {
        return meter;
    }

    @Override
    public String getShortResultStat() {
        return String.format("%s %s videos", getChannelId(), meter.getCounter());
    }
}
