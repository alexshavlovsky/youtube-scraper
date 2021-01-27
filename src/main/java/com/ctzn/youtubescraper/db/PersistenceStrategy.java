package com.ctzn.youtubescraper.db;

import org.hibernate.Session;

@FunctionalInterface
public interface PersistenceStrategy {
    void execute(Session session);
}
