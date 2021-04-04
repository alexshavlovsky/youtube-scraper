package com.ctzn.youtubescraper.addons.hibernateconnector.repository;

import com.ctzn.youtubescraper.core.persistence.dto.ChannelDTO;
import com.ctzn.youtubescraper.addons.hibernateconnector.DomainMapper;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.ChannelEntity;
import org.hibernate.Session;

public class ChannelRepository {

    public static ChannelEntity findById(String channelId, Session session) {
        return session.get(ChannelEntity.class, channelId);
    }

    public static ChannelEntity saveOrUpdateAndGet(ChannelDTO channelDTO, Session session) {
        ChannelEntity persistentChanel = findById(channelDTO.getChannelId(), session);
        if (persistentChanel == null) {
            ChannelEntity transientChanel = ChannelEntity.fromChannelDTO(channelDTO);
            session.persist(transientChanel);
            return transientChanel;
        } else {
            DomainMapper.getInstance().map(channelDTO, persistentChanel);
            return persistentChanel;
        }
    }

}
