package com.ctzn.youtubescraper.iterator;

import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.model.channelvideos.VideosGrid;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@Log
public class VideoContextIterator {

    private final IterableVideoContext context;
    private final List<VideoDTO> acc = new ArrayList<>();

    public VideoContextIterator(IterableVideoContext context) {
        this.context = context;
        traverse();
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
        acc.addAll(videos);
        log.fine(context.getShortResultStat());
    }
}
