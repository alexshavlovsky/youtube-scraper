package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.util.concurrent.TimeUnit;

public class HibernateExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        PersistenceChannelRunner.newBuilder(channelId)
                .nThreads(10).timeout(1).timeUnit(TimeUnit.HOURS)
                .getBuilder().build().call();
    }

}
