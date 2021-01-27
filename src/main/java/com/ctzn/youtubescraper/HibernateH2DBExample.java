package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.runner.PersistenceChannelRunner;

import java.util.concurrent.Callable;

public class HibernateH2DBExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCXe92kCAWPdWRW7Ylfku_rw";
        Callable<Void> runner = new PersistenceChannelRunner(channelId);
        runner.call();
    }
}
