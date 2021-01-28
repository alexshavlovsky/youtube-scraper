package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.DefaultPersistenceContext;
import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class HibernateH2DBExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCXe92kCAWPdWRW7Ylfku_rw";
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
