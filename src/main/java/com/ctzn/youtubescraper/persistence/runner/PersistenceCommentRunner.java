package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.handler.CommentCollector;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.CommentEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PersistenceCommentRunner implements Runnable {

    private final String videoId;
    private final Map<String, VideoEntity> videoEntityMap;
    private final PersistenceContext persistenceContext;
    private final boolean sortNewestCommentsFirst;
    private final int totalCommentCountLimit;
    private final int replyThreadCountLimit;


    PersistenceCommentRunner(String videoId, Map<String, VideoEntity> videoEntityMap, PersistenceContext persistenceContext, boolean sortNewestCommentsFirst, int totalCommentCountLimit, int replyThreadCountLimit) {
        this.videoId = videoId;
        this.videoEntityMap = videoEntityMap;
        this.persistenceContext = persistenceContext;
        this.sortNewestCommentsFirst = sortNewestCommentsFirst;
        this.totalCommentCountLimit = totalCommentCountLimit;
        this.replyThreadCountLimit = replyThreadCountLimit;
    }

    @Override
    public void run() {
        CommentCollector commentCollector = new CommentCollector();
        CommentRunnerFactory.newInstance(videoId, List.of(commentCollector), sortNewestCommentsFirst, totalCommentCountLimit, replyThreadCountLimit).run();
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
