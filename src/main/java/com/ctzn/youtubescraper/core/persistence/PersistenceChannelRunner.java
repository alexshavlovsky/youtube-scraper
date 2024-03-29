package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.config.ExecutorCfg;
import com.ctzn.youtubescraper.core.config.VideoIteratorCfg;
import com.ctzn.youtubescraper.core.persistence.dto.StatusCode;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
import lombok.extern.java.Log;

import java.util.List;
import java.util.StringJoiner;

@Log
public class PersistenceChannelRunner extends PersistenceRunner {

    private final String channelId;

    public PersistenceChannelRunner(String channelId, PersistenceService persistenceService, ExecutorCfg executorCfg, CommentOrderCfg commentOrderCfg, VideoIteratorCfg videoIteratorCfg, CommentIteratorCfg commentIteratorCfg) {
        super(persistenceService, executorCfg, commentOrderCfg, videoIteratorCfg, commentIteratorCfg);
        this.channelId = channelId;
    }

    @Override
    public Void call() throws Exception {
        persistenceService.logChannel(channelId, StatusCode.PASSED_TO_WORKER, toString());
        try {
            List<VideoDTO> videos = grabChannelData(channelId);
            grabComments(videos, channelId);
            persistenceService.logChannel(channelId, StatusCode.DONE, "Videos processed: " + videos.size());
        } catch (Exception e) {
            log.severe(e.getMessage());
            persistenceService.logChannel(channelId, StatusCode.ERROR, e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("channelId='" + channelId + "'")
                .add(super.toString())
                .toString();
    }

}
