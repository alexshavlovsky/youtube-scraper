package com.ctzn.youtubescraper.persistence.repository;

import com.ctzn.youtubescraper.model.DomainMapper;
import com.ctzn.youtubescraper.persistence.entity.WorkerLogEntity;
import org.hibernate.Session;

public class WorkerLogRepository {

    public static void save(WorkerLogEntity logEntry, Session session) {
        logEntry.setId((Long) session.save(logEntry));
    }

    public static void saveOrUpdate(WorkerLogEntity logEntry, Session session) {
        WorkerLogEntity entity = session.get(WorkerLogEntity.class, logEntry.getId());
        if (entity == null) session.save(logEntry);
        else DomainMapper.getInstance().map(logEntry, entity);
    }
}
