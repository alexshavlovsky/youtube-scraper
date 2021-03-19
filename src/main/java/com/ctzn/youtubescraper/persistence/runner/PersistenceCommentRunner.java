package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.handler.DataCollector;
import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import com.ctzn.youtubescraper.persistence.entity.CommentEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.persistence.entity.WorkerLogEntity;
import com.ctzn.youtubescraper.persistence.repository.CommentRepository;
import com.ctzn.youtubescraper.persistence.repository.WorkerLogRepository;
import com.ctzn.youtubescraper.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.config.CommentOrderCfg;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

class PersistenceCommentRunner implements Runnable {

    private final String videoId;
    private final Map<String, VideoEntity> videoEntityMap;
    private final PersistenceContext persistenceContext;
    private final CommentOrderCfg commentOrderCfg;
    private final CommentIteratorCfg commentIteratorCfg;

    PersistenceCommentRunner(String videoId, Map<String, VideoEntity> videoEntityMap, PersistenceContext persistenceContext, CommentOrderCfg commentOrderCfg, CommentIteratorCfg commentIteratorCfg) {
        this.videoId = videoId;
        this.videoEntityMap = videoEntityMap;
        this.persistenceContext = persistenceContext;
        this.commentOrderCfg = commentOrderCfg;
        this.commentIteratorCfg = commentIteratorCfg;
    }

    @Override
    public void run() {
        WorkerLogEntity logEntry = new WorkerLogEntity(null, videoId, new Date(), null, "STARTED", toString());
        persistenceContext.commitTransaction(session -> WorkerLogRepository.save(logEntry, session));
        DataCollector<CommentDTO> collector = new DataCollector<>();
        CommentRunnerFactory.newInstance(videoId, collector, commentOrderCfg, commentIteratorCfg).run();

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
        return new StringJoiner(", ", PersistenceCommentRunner.class.getSimpleName() + "[", "]")
                .add("commentOrderCfg=" + commentOrderCfg)
                .add("commentIteratorCfg=" + commentIteratorCfg)
                .toString();
    }

}
