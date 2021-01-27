package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.db.PersistenceContext;
import com.ctzn.youtubescraper.entity.CommentEntity;
import com.ctzn.youtubescraper.entity.VideoEntity;
import com.ctzn.youtubescraper.handler.CommentCollector;
import com.ctzn.youtubescraper.model.CommentDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PersistenceCommentRunner implements Runnable {

    private final String videoId;
    private final Map<String, VideoEntity> videoEntityMap;

    PersistenceCommentRunner(String videoId, Map<String, VideoEntity> videoEntityMap) {
        this.videoId = videoId;
        this.videoEntityMap = videoEntityMap;
    }

    @Override
    public void run() {
        CommentCollector commentCollector = new CommentCollector();
        new CommentNewestFirstRunner(videoId, List.of(commentCollector)).run();
        List<CommentDTO> comments = commentCollector.getComments();

        List<CommentEntity> commentEntities = comments.stream().filter(c -> c.getParentCommentId() == null)
                .map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap, null)).collect(Collectors.toList());
        Map<String, CommentEntity> commentEntityMap = commentEntities.stream().collect(Collectors.toMap(CommentEntity::getCommentId, e -> e));
        List<CommentEntity> replyEntities = comments.stream().filter(c -> c.getParentCommentId() != null)
                .map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap, commentEntityMap)).collect(Collectors.toList());

        PersistenceContext.commitTransaction(session -> {
            commentEntities.forEach(session::saveOrUpdate);
            replyEntities.forEach(session::saveOrUpdate);
        });
    }
}
