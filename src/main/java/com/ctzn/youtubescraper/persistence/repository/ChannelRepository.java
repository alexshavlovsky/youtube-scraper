package com.ctzn.youtubescraper.persistence.repository;

import com.ctzn.youtubescraper.model.DomainMapper;
import com.ctzn.youtubescraper.persistence.entity.ChannelEntity;
import org.hibernate.Session;

public class ChannelRepository {

    public static void saveOrUpdate(ChannelEntity channel, Session session) {
        ChannelEntity entity = session.get(ChannelEntity.class, channel.getChannelId());
        if (entity == null) session.save(channel);
        else DomainMapper.getInstance().map(channel, entity);
    }
}
