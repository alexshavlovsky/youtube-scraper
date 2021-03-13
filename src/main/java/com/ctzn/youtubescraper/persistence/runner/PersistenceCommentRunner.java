package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.handler.DataCollector;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.CommentEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.persistence.entity.WorkerLogEntity;
import com.ctzn.youtubescraper.persistence.repository.CommentRepository;
import com.ctzn.youtubescraper.persistence.repository.WorkerLogRepository;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

import java.util.Date;
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
        WorkerLogEntity logEntry = new WorkerLogEntity(null, videoId, new Date(), null, "STARTED", toString());
        persistenceContext.commitTransaction(session -> WorkerLogRepository.save(logEntry, session));
        DataCollector<CommentDTO> collector = new DataCollector<>();
        CommentRunnerFactory.newInstance(videoId, collector, sortNewestCommentsFirst, totalCommentCountLimit, replyThreadCountLimit).run();

        List<CommentEntity> commentEntities = collector.stream().filter(c -> c.getParentCommentId() == null)
                .map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap, null)).collect(Collectors.toList());
        Map<String, CommentEntity> commentEntityMap = commentEntities.stream().collect(Collectors.toMap(CommentEntity::getCommentId, e -> e));
        List<CommentEntity> replyEntities = collector.stream().filter(c -> c.getParentCommentId() != null)
                .map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap, commentEntityMap)).collect(Collectors.toList());

        persistenceContext.commitTransaction(session -> {
            commentEntities.forEach(comment -> CommentRepository.saveOrUpdate(comment, session));
            replyEntities.forEach(comment -> CommentRepository.saveOrUpdate(comment, session));
        });

        logEntry.setFinishedDate(new Date());
        logEntry.setStatus("FINISHED: commentCount=" + collector.size());
        persistenceContext.commitTransaction(session -> WorkerLogRepository.saveOrUpdate(logEntry, session));
    }

    @Override
    public String toString() {
        return "PersistenceCommentRunner{" +
                "sortNewestCommentsFirst=" + sortNewestCommentsFirst +
                ", totalCommentCountLimit=" + totalCommentCountLimit +
                ", replyThreadCountLimit=" + replyThreadCountLimit +
                '}';
    }
}
