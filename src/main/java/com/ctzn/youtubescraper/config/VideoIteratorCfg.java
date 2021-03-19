package com.ctzn.youtubescraper.config;

import lombok.Data;

@Data
public class VideoIteratorCfg {

    public static final int PROCESS_ALL_VIDEOS = 0;

    public int videoCountLimit = PROCESS_ALL_VIDEOS;

}
