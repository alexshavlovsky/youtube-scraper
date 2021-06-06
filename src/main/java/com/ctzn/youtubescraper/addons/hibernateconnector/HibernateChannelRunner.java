package com.ctzn.youtubescraper.addons.hibernateconnector;

import com.ctzn.youtubescraper.core.persistence.PersistenceRunnerStepBuilder;

public class HibernateChannelRunner {
    public static PersistenceRunnerStepBuilder.ExecutorStep newBuilder(String channelId) {
        return PersistenceRunnerStepBuilder.newChannelRunnerBuilder(channelId, PersistenceServiceFactory.getInstance());
    }
}
