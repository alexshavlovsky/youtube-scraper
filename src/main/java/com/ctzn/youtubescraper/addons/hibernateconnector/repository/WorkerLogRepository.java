package com.ctzn.youtubescraper.addons.hibernateconnector.repository;

import com.ctzn.youtubescraper.addons.hibernateconnector.entity.WorkerLogEntity;
import org.hibernate.Session;

public class WorkerLogRepository {

    public static void save(WorkerLogEntity logEntry, Session session) {
        session.persist(logEntry);
    }

}
