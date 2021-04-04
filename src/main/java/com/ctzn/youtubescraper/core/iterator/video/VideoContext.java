package com.ctzn.youtubescraper.core.iterator.video;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.http.IterableHttpClient;
import com.ctzn.youtubescraper.core.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
import lombok.extern.java.Log;

@Log
public class VideoContext {

    private final VideoContextMeter meter = new VideoContextMeter();
    private final IterableHttpClient<VideosGrid> client;
    private final String channelId;
    private VideosGrid grid;

    public VideoContext(IterableHttpClient<VideosGrid> client) {
        this.client = client;
        this.channelId = client.getParentId();
        this.grid = client.getInitial();
    }

    public void traverse(DataHandler<VideoDTO> handler) throws ScrapperInterruptedException {
        handleGrid(handler);
        while (grid != null && grid.hasContinuation()) {
            if (Thread.currentThread().isInterrupted())
                throw new ScrapperInterruptedException("Video context iterator thread has been interrupted");
            nextGrid();
            handleGrid(handler);
        }
    }

    private void handleGrid(DataHandler<VideoDTO> handler) {
        if (grid != null) {
            handler.accept(grid.getVideos(channelId));
            meter.update(grid.countContentPieces());
            log.fine(getShortResultStat());
        }
    }

    public String getShortResultStat() {
        return String.format("%s #%s %s videos", channelId, meter.getContinuationCounter(), meter.getCounter());
    }

    private void nextGrid() throws ScrapperInterruptedException {
        try {
            grid = client.requestNext(grid.getContinuation());
        } catch (ScraperHttpException | ScraperParserException e) {
            log.warning(channelId + " " + e.toString());
            grid = null;
        }
    }

}
