package com.ctzn.youtubescraper.iterator.video;

import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.handler.DataHandler;
import com.ctzn.youtubescraper.model.channelvideos.VideoDTO;
import lombok.extern.java.Log;

@Log
public class VideoContextIterator {

    private final IterableVideoContext context;
    private final DataHandler<VideoDTO> handler;

    public VideoContextIterator(IterableVideoContext context, DataHandler<VideoDTO> handler) {
        this.context = context;
        this.handler = handler;
    }

    public void traverse() throws ScrapperInterruptedException {
        traverse(context);
    }

    private void traverse(IterableVideoContext context) throws ScrapperInterruptedException {
        while (true) {
            if (context.hasGrid()) {
                handler.accept(context.getVideos());
                log.fine(context::getShortResultStat);
            }
            if (Thread.currentThread().isInterrupted())
                throw new ScrapperInterruptedException("Thread has been interrupted");
            if (context.hasContinuation()) context.nextGrid(context.getContinuationData());
            else return;
        }
    }

}
