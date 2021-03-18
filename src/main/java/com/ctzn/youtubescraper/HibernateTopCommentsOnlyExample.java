package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.persistence.runner.PersistenceChannelRunner;

import java.util.concurrent.TimeUnit;

public class HibernateTopCommentsOnlyExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws Exception {
        String channelId = "UCsAw3WynQJMm7tMy093y37A";
        PersistenceChannelRunner.newBuilder(channelId)
                .nThreads(30).timeout(1).timeUnit(TimeUnit.HOURS)
                .videoCountLimit(100)
                .sortNewestCommentsFirst(false)
                .commentCountPerVideoLimit(2000).replyThreadCountLimit(10)
                .getBuilder().build().call();
    }

    // top commentators:
    // select distinct(channel_id), author_text, count(comment_id) as cnt from comments group by channel_id, author_text order by cnt desc

    // most liked comments:
    // select * from (((select * from comments where like_count > 1000) order by like_count desc) limit 100) as liked inner join videos on liked.video_id=videos.video_id
}
