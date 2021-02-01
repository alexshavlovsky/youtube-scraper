package com.ctzn.youtubescraper.handler;

import com.ctzn.youtubescraper.model.channelvideos.VideoDTO;

import java.util.ArrayList;
import java.util.List;

public class VideoCollector implements VideoHandler {

    private final List<VideoDTO> videos = new ArrayList<>();

    public VideoCollector() {
    }

    @Override
    public void handle(List<VideoDTO> items) {
        videos.addAll(items);
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }
}
