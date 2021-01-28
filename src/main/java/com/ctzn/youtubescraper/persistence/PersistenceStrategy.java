package com.ctzn.youtubescraper.persistence;

import org.hibernate.Session;

@FunctionalInterface
public interface PersistenceStrategy {
    void execute(Session session);
}
