package com.ctzn.youtubescraper.addons.hibernateconnector;

import com.ctzn.youtubescraper.core.persistence.PersistenceChannelRunnerStepBuilder;

public class HibernateChannelRunner {
    public static PersistenceChannelRunnerStepBuilder.ExecutorStep newBuilder(String channelId) {
        return PersistenceChannelRunnerStepBuilder.newBuilder(channelId, PersistenceServiceFactory.getInstance());
    }
}
