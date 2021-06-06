package com.ctzn.youtubescraper.examples.hibernate;

import com.ctzn.youtubescraper.addons.hibernateconnector.HibernateChannelRunner;

import java.time.Duration;

public class HibernateExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCksTNgiRyQGwi2ODBie8HdA";
        HibernateChannelRunner.newBuilder(channelId)
                .withExecutor(20, Duration.ofHours(1))
                .processAllComments().build().call();
    }

}
