package com.ctzn.youtubescraper.config;

import lombok.Getter;

@Getter
public class VideoIteratorCfg {

    private final CountLimit videoCountLimit = new CountLimit();

    private VideoIteratorCfg() {
    }

    private VideoIteratorCfg(int videoCountLimit) {
        this.videoCountLimit.set(videoCountLimit);
    }

    public static VideoIteratorCfg newInstance() {
        return new VideoIteratorCfg();
    }

    public static VideoIteratorCfg newInstance(int videoCountLimit) {
        return new VideoIteratorCfg(videoCountLimit);
    }

    @Override
    public String toString() {
        return "videoCountLimit=" + videoCountLimit;
    }

}
