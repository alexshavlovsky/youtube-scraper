package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.DefaultPersistenceContext;
import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class HibernateExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        Callable<Void> runner = new PersistenceChannelRunner(
                channelId,
                new DefaultPersistenceContext(),
                10,
                1,
                TimeUnit.HOURS
        );
        runner.call();
    }
}
