package com.ctzn.youtubescraper.iterator.video;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.http.IterableHttpClient;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;
import lombok.extern.java.Log;

@Log
public class VideoContext implements IterableVideoContext {

    private final VideoContextMeter meter = new VideoContextMeter();
    private final IterableHttpClient<VideosGrid> client;
    private VideosGrid grid;

    public VideoContext(IterableHttpClient<VideosGrid> client) {
        this.client = client;
        this.grid = client.getInitial();
        if (grid != null) meter.update(grid.countContentPieces());
    }

    @Override
    public String getChannelId() {
        return client.getParentId();
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
        try {
            grid = client.requestNext(continuationData);
            if (grid == null) return;
            int itemsCount = grid.countContentPieces();
            meter.update(itemsCount);
        } catch (ScraperHttpException | ScraperParserException e) {
            log.warning(client.getParentId() + " " + e.toString());
            grid = null;
        } catch (ScrapperInterruptedException e) {
            grid = null;
        }
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
        return String.format("%s #%s %s videos", getChannelId(), meter.getContinuationCounter(), meter.getCounter());
    }
}
