package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.db.SessionFactorySource;
import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;
import com.ctzn.youtubescraper.runner.RunnerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class H2Test {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException {
//        String chanelId = "UCksTNgiRyQGwi2ODBie8HdA";
//        ChannelVideosCollector collector = new ChannelVideosCollector(chanelId);
//        ChannelDTO channel = collector.call();
//
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//        channel.videos.stream().map(VideoDTO::getVideoId)
//                .map(RunnerFactory::newNewestCommentsFirstFileAppenderRunner).forEach(executor::submit);
//        executor.shutdown();

        SessionFactory factory = SessionFactorySource.getInstance().getSessionFactory();
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
//            CommentDTO commentEntity = new CommentDTO(
//                    "ddddfd",
//                    "ddsdsds",
//                    "fdfdfd",
//                    "dfdfd",
//                    "fdfdfd",
//                    "rterterter",
//                    55,
//                    567,
//                    "ffg");
//            session.persist(commentEntity);
            tx.commit();
        } catch (PersistenceException ex) {
            if (tx != null) tx.rollback();
        }
    }
}
