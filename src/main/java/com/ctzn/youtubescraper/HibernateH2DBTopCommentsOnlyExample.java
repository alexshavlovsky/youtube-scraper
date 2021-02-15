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
                30, // create 30 worker threads
                1, TimeUnit.HOURS, // forcibly interrupt the runner if it lasts more than 1 hour
                false, // request top comments (default youtube behaviour when you scroll down a page)
                // youtube comment API has a limitation: you can't request more than approximately 80 continuations
                // in the "top comments first" sorting mode (20 comments per continuation + replies threads)
                // if you switch to "newest comments first" mode it seems that there are no limitations as for now
                2000, // request no more than than 2000 comments per video
                10,  // request no more than than 10 replies per comment
                100 // process 100 most recent videos
        );
        runner.call();
    }

    // top commentators:
    // select distinct(author_url), author_text, count(comment_id) as cnt from comments group by author_url, author_text order by cnt desc

    // most liked comments:
    // select * from (((select * from comments where like_count > 1000) order by like_count desc) limit 100) as liked inner join videos on liked.video_id=videos.video_id
}
