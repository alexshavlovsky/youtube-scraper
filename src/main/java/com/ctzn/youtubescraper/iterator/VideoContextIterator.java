package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.handler.VideoHandler;
import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import lombok.extern.java.Log;

import java.util.List;

@Log
public class VideoContextIterator {

    private final IterableVideoContext context;
    private final List<VideoHandler> handlers;

    public VideoContextIterator(IterableVideoContext context, List<VideoHandler> handlers) {
        this.context = context;
        this.handlers = handlers;
    }

    public void traverse() {
        traverse(context);
    }

    private void traverse(IterableVideoContext context) {
        while (true) {
            if (context.hasGrid()) handle(context);
            if (context.hasContinuation()) context.nextGrid(context.getContinuationData());
            else return;
        }
    }

    private void handle(IterableVideoContext context) {
        VideosGrid grid = context.getGrid();
        List<VideoDTO> videos = grid.getVideos(context.getChannelId());
        handlers.forEach(handler -> handler.handle(videos));
        log.fine(context.getShortResultStat());
    }
}
