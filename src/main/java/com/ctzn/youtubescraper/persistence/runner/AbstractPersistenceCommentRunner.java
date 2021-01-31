package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.handler.CommentCollector;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.CommentEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class AbstractPersistenceCommentRunner implements Runnable {

    private final String videoId;
    private final Map<String, VideoEntity> videoEntityMap;
    private final PersistenceContext persistenceContext;

    AbstractPersistenceCommentRunner(String videoId, Map<String, VideoEntity> videoEntityMap, PersistenceContext persistenceContext) {
        this.videoId = videoId;
        this.videoEntityMap = videoEntityMap;
        this.persistenceContext = persistenceContext;
    }

    abstract Runnable newCommentRunner(String videoId, List<CommentHandler> handlers);

    @Override
    public void run() {
        CommentCollector commentCollector = new CommentCollector();
        newCommentRunner(videoId, List.of(commentCollector)).run();
        List<CommentDTO> comments = commentCollector.getComments();

        List<CommentEntity> commentEntities = comments.stream().filter(c -> c.getParentCommentId() == null)
                .map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap, null)).collect(Collectors.toList());
        Map<String, CommentEntity> commentEntityMap = commentEntities.stream().collect(Collectors.toMap(CommentEntity::getCommentId, e -> e));
        List<CommentEntity> replyEntities = comments.stream().filter(c -> c.getParentCommentId() != null)
                .map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap, commentEntityMap)).collect(Collectors.toList());

        persistenceContext.commitTransaction(session -> {
            commentEntities.forEach(session::saveOrUpdate);
            replyEntities.forEach(session::saveOrUpdate);
        });
    }
}
