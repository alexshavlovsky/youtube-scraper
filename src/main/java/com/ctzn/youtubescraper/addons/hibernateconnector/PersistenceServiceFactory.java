package com.ctzn.youtubescraper.addons.hibernateconnector;

import com.ctzn.youtubescraper.core.persistence.PersistenceService;

public class PersistenceServiceFactory {

    private static class SingletonHolder {
        private final static PersistenceService SESSION_FACTORY = new PersistenceServiceImpl(new DefaultPersistenceContext());
    }

    static PersistenceService getInstance() {
        return SingletonHolder.SESSION_FACTORY;
    }

}
