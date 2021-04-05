package com.ctzn.youtubescraper.addons.hibernateconnector;

import com.ctzn.youtubescraper.addons.hibernateconnector.entity.*;
import com.ctzn.youtubescraper.addons.hibernateconnector.repository.ChannelRepository;
import com.ctzn.youtubescraper.addons.hibernateconnector.repository.CommentRepository;
import com.ctzn.youtubescraper.addons.hibernateconnector.repository.VideoRepository;
import com.ctzn.youtubescraper.addons.hibernateconnector.repository.WorkerLogRepository;
import com.ctzn.youtubescraper.core.persistence.PersistenceService;
import com.ctzn.youtubescraper.core.persistence.dto.*;
import lombok.extern.java.Log;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

@Log
public class PersistenceServiceImpl implements PersistenceService {

    private final PersistenceContext persistenceContext;

    public PersistenceServiceImpl(PersistenceContext persistenceContext) {
        this.persistenceContext = persistenceContext;
    }

    @Override
    public void saveChannelVideos(ChannelVideosDTO channelVideos) {
        persistenceContext.commitTransaction(session -> {
            ChannelEntity persistentChanel = ChannelRepository.saveOrUpdateAndGet(channelVideos.getChannel(), session);
            VideoRepository.saveAll(channelVideos.getVideos(), persistentChanel, session);
        });
    }

    @Override
    public void saveVideoComments(String videoId, List<CommentDTO> comments, List<CommentDTO> replies) {
        persistenceContext.commitTransaction(session -> {
            VideoEntity videoEntity = VideoRepository.findById(videoId, session);
            if (videoEntity == null) {
                log.warning("Can't save comments. The parent video doesn't exist: " + videoId);
                return;
            }
            Map<String, CommentEntity> parentMap = CommentRepository.saveAll(comments, null, videoEntity, session);
            CommentRepository.saveAll(replies, parentMap, videoEntity, session);
        });
    }

    @Override
    public void updateVideoTotalCommentCount(String videoId, int totalCommentCount) {
        persistenceContext.commitTransaction(session -> {
            VideoEntity videoEntity = VideoRepository.findById(videoId, session);
            if (videoEntity == null) {
                log.warning("Can't update video total comment count. The video doesn't exist: " + videoId);
                return;
            }
            videoEntity.setTotalCommentCount(totalCommentCount);
        });
    }

    @Override
    public void saveWorkerLog(WorkerLogDTO logEntry) {
        persistenceContext.commitTransaction(session -> saveWorkerLog(logEntry, session));
    }

    @Override
    public void setChannelStatus(String channelId, ContextStatusDTO status) {
        persistenceContext.commitTransaction(session -> setChannelStatus(channelId, status, session));
    }

    @Override
    public void setVideoStatus(String videoId, ContextStatusDTO status) {
        persistenceContext.commitTransaction(session -> setVideoStatus(videoId, status, session));
    }

    @Override
    public void logVideo(String videoId, StatusCode statusCode, String statusMessage) {
        persistenceContext.commitTransaction(session -> {
            ContextStatusDTO contextStatus = new ContextStatusDTO(statusCode, statusMessage);
            saveWorkerLog(new WorkerLogDTO(videoId, contextStatus), session);
            setVideoStatus(videoId, contextStatus, session);
        });
    }

    @Override
    public void logChannel(String channelId, StatusCode statusCode, String statusMessage) {
        persistenceContext.commitTransaction(session -> {
            ContextStatusDTO contextStatus = new ContextStatusDTO(statusCode, statusMessage);
            saveWorkerLog(new WorkerLogDTO(channelId, contextStatus), session);
            setChannelStatus(channelId, contextStatus, session);
        });
    }

    private void saveWorkerLog(WorkerLogDTO logEntry, Session session) {
        WorkerLogRepository.save(WorkerLogEntity.fromWorkerLogDTO(logEntry), session);
    }

    private void setChannelStatus(String channelId, ContextStatusDTO status, Session session) {
        ChannelEntity channel = ChannelRepository.findById(channelId, session);
        if (channel == null) {
            channel = ChannelEntity.newPendingChannel(channelId);
            session.persist(channel);
        }
        channel.setContextStatus(ContextStatus.fromContextStatusDTO(status));
    }

    private void setVideoStatus(String videoId, ContextStatusDTO status, Session session) {
        VideoEntity video = VideoRepository.findById(videoId, session);
        if (video == null) {
            log.warning("Can't save comments. The parent video doesn't exist: " + videoId);
            return;
        }
        video.setContextStatus(ContextStatus.fromContextStatusDTO(status));
    }

}
