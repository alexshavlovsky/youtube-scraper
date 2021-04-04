package com.ctzn.youtubescraper.addons.hibernateconnector.repository;

import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
import com.ctzn.youtubescraper.addons.hibernateconnector.DomainMapper;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.ChannelEntity;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.VideoEntity;
import org.hibernate.Session;

import java.util.Collection;

public class VideoRepository {

    public static VideoEntity findById(String videoId, Session session) {
        return session.get(VideoEntity.class, videoId);
    }

    public static VideoEntity saveOrUpdateAndGet(VideoDTO videoDTO, ChannelEntity channelEntity, Session session) {
        VideoEntity persistentVideo = findById(videoDTO.getVideoId(), session);
        if (persistentVideo == null) {
            VideoEntity transientVideo = VideoEntity.fromVideoDTO(videoDTO, channelEntity);
            session.persist(transientVideo);
            return transientVideo;
        } else {
            DomainMapper.getInstance().map(videoDTO, persistentVideo);
            return persistentVideo;
        }
    }

    public static void saveAll(Collection<VideoDTO> videos, ChannelEntity channelEntity, Session session) {
        videos.forEach(video -> saveOrUpdateAndGet(video, channelEntity, session));
    }

}
