package com.ctzn.youtubescraper.model.channelvideos;

public class BrowseApiResponse {
    public BrowseResponse response;
    public String xsrf_token;

    public VideosGrid getVideosGrid() {
        return response.continuationContents.gridContinuation;
    }

    public String getToken() {
        return xsrf_token;
    }
}
