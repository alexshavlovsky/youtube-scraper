package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.time.Duration;

public class HibernateExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        PersistenceChannelRunner.newBuilder(channelId)
                .withExecutor(20, Duration.ofHours(1))
                .processAllChannelComments().build().call();
    }

}
