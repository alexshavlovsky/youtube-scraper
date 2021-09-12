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
import java.util.stream.Collectors;

@Log
public class PersistenceVideoListRunner extends PersistenceRunner {
    private final List<String> videoIds;

    public PersistenceVideoListRunner(List<String> videoIds, PersistenceService persistenceService, ExecutorCfg executorCfg, CommentOrderCfg commentOrderCfg, VideoIteratorCfg videoIteratorCfg, CommentIteratorCfg commentIteratorCfg) {
        super(persistenceService, executorCfg, commentOrderCfg, videoIteratorCfg, commentIteratorCfg);
        this.videoIds = videoIds;
    }

    @Override
    public Void call() throws Exception {
        if (videoIds.size() == 0) return null;
        try {
            List<VideoDTO> videos = videoIds.stream()
                    .map(id -> new VideoDTO(null, id, null, null, null, null))
                    .collect(Collectors.toList());
            String threadPrefix = videoIds.get(0);
            if (videoIds.size() > 1) threadPrefix += "..." + videoIds.get(videos.size() - 1);
            grabComments(videos, threadPrefix);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("videoIds=" + videoIds.toString())
                .add(super.toString())
                .toString();
    }

}
