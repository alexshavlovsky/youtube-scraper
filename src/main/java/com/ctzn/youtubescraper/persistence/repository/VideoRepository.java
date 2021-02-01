package com.ctzn.youtubescraper.persistence.repository;

import com.ctzn.youtubescraper.model.DomainMapper;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import org.hibernate.Session;

public class VideoRepository {

    public static void saveOrUpdate(VideoEntity video, Session session) {
        VideoEntity entity = session.get(VideoEntity.class, video.getVideoId());
        if (entity == null) session.save(video);
        else DomainMapper.getInstance().map(video, entity);
    }
}
