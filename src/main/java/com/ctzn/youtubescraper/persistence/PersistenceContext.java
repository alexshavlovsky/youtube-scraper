package com.ctzn.youtubescraper.persistence;

public interface PersistenceContext {
    void commitTransaction(PersistenceStrategy persistenceStrategy);
}
