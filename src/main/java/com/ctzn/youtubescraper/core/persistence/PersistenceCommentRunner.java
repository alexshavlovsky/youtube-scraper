package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.handler.DataCollector;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
import com.ctzn.youtubescraper.core.persistence.dto.StatusCode;
import lombok.extern.java.Log;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Log
class PersistenceCommentRunner implements Runnable {

    private final String videoId;
    private final PersistenceService persistenceService;
    private final CommentOrderCfg commentOrderCfg;
    private final CommentIteratorCfg commentIteratorCfg;

    PersistenceCommentRunner(String videoId, PersistenceService persistenceService, CommentOrderCfg commentOrderCfg, CommentIteratorCfg commentIteratorCfg) {
        this.videoId = videoId;
        this.persistenceService = persistenceService;
        this.commentOrderCfg = commentOrderCfg;
        this.commentIteratorCfg = commentIteratorCfg;
    }

    @Override
    public void run() {
        persistenceService.logVideo(videoId, StatusCode.PASSED_TO_WORKER, toString());
        try {
            DataCollector<CommentDTO> collector = new DataCollector<>();
            int totalCommentCount = CommentRunnerFactory.newInstance(videoId, collector, commentOrderCfg, commentIteratorCfg).call(
                    commentCount -> persistenceService.updateVideoTotalCommentCount(videoId, commentCount)
            );

            List<CommentDTO> comments = collector.stream().filter(c -> c.getParentCommentId() == null).collect(Collectors.toList());
            List<CommentDTO> replies = collector.stream().filter(c -> c.getParentCommentId() != null).collect(Collectors.toList());
            int cs = comments.size(), rs = replies.size();

            persistenceService.saveVideoComments(videoId, comments, replies);

            persistenceService.logVideo(videoId, StatusCode.DONE,
                    String.format("total: %d of %d, comments: %d, replies: %d", cs + rs, totalCommentCount, cs, rs));
        } catch (Exception e) {
            log.severe("ERROR " + videoId + ' ' + e.getMessage());
            persistenceService.logVideo(videoId, StatusCode.ERROR, e.getMessage());
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ")
                .add("videoId='" + videoId + "'")
                .add(commentOrderCfg.toString())
                .add(commentIteratorCfg.toString())
                .toString();
    }

}
