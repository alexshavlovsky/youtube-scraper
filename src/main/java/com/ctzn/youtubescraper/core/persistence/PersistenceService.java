package com.ctzn.youtubescraper.core.persistence;

import com.ctzn.youtubescraper.core.persistence.dto.*;

import java.util.List;

public interface PersistenceService {

    void saveChannelVideos(ChannelVideosDTO channelVideos);

    void saveVideoComments(String videoId, List<CommentDTO> comments, List<CommentDTO> replies);

    void updateVideoTotalCommentCount(String videoId, int totalCommentCount);

    void saveWorkerLog(WorkerLogDTO logEntry);

    void setChannelStatus(String channelId, ContextStatusDTO status);

    void setVideoStatus(String videoId, ContextStatusDTO status);

    default void logVideo(String videoId, StatusCode statusCode, String statusMessage) {
        ContextStatusDTO contextStatus = new ContextStatusDTO(statusCode, statusMessage);
        saveWorkerLog(new WorkerLogDTO(videoId, contextStatus));
        setVideoStatus(videoId, contextStatus);
    }

    default void logChannel(String channelId, StatusCode statusCode, String statusMessage) {
        ContextStatusDTO contextStatus = new ContextStatusDTO(statusCode, statusMessage);
        saveWorkerLog(new WorkerLogDTO(channelId, contextStatus));
        setChannelStatus(channelId, contextStatus);
    }

}
