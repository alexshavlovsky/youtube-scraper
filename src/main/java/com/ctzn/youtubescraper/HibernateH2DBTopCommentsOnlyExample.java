package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.DefaultPersistenceContext;
import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class HibernateH2DBTopCommentsOnlyExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCsAw3WynQJMm7tMy093y37A";
        Callable<Void> runner = new PersistenceChannelRunner(
                channelId,
                new DefaultPersistenceContext(),
                50,
                1, TimeUnit.HOURS,
                false,
                1000,
                100
        );
        runner.call();
    }

    // top commentators:
    // select distinct(author_url), author_text, count(comment_id) as cnt from comments group by author_url, author_text order by cnt desc
}
