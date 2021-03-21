package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.NativeQueryExecutor;
import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.time.Duration;

public class HibernateRecursiveExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    private final static String nextChannelQuery = "SELECT channel_id FROM (select distinct(channel_id), author_text, count(comment_id) as cnt from comments group by channel_id, author_text order by cnt desc) WHERE channel_id NOT IN (select channel_id from channels) limit 1";
    private final static String columnAlias = "channel_id";

    static String getNextChannelId() {
        return NativeQueryExecutor.getSingleStringResultByColumnAlias(nextChannelQuery, columnAlias);
    }

    public static void main(String[] args) throws Exception {
        String channelId;
        while ((channelId = getNextChannelId()) != null) {
            PersistenceChannelRunner.newBuilder(channelId)
                    .withExecutor(50, Duration.ofHours(1))
                    .processAllChannelComments().build().call();
        }
    }

}
