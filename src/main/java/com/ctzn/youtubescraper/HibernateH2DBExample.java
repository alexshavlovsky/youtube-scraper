package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.db.SessionFactorySource;
import com.ctzn.youtubescraper.entity.ChannelEntity;
import com.ctzn.youtubescraper.entity.CommentEntity;
import com.ctzn.youtubescraper.entity.VideoEntity;
import com.ctzn.youtubescraper.exception.ScraperException;
import com.ctzn.youtubescraper.handler.CommentCollector;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.model.CommentDTO;
import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.runner.ChannelVideosCollector;
import com.ctzn.youtubescraper.runner.CommentNewestFirstRunner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HibernateH2DBExample {

    static {
        System.setProperty("java.util.logging.config.file", "logging-info.properties");
    }

    public static void main(String[] args) throws ScraperException, InterruptedException {
        // grab channel metadata and videos list
        String chanelId = "UCksTNgiRyQGwi2ODBie8HdA";
        ChannelVideosCollector collector = new ChannelVideosCollector(chanelId);
        ChannelDTO channel = collector.call();
        List<VideoDTO> videos = channel.getVideos();

        // grab comments
        CommentCollector commentCollector = new CommentCollector();
        List<CommentHandler> handlers = List.of(commentCollector);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        videos.stream().map(VideoDTO::getVideoId).map(videoId -> new CommentNewestFirstRunner(videoId, handlers)).forEach(executor::submit);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        List<CommentDTO> comments = commentCollector.getComments();

        // prepare entities
        ChannelEntity channelEntity = ChannelEntity.fromChannelDTO(channel);
        List<VideoEntity> videoEntities = channel.getVideos().stream().map(v -> VideoEntity.fromVideoDTO(v, channelEntity)).collect(Collectors.toList());
        Map<String, VideoEntity> videoEntityMap = videoEntities.stream().collect(Collectors.toMap(VideoEntity::getVideoId, e -> e));
        List<CommentEntity> commentEntities = comments.stream().filter(c -> c.getParentCommentId() == null).map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap.get(c.getVideoId()))).collect(Collectors.toList());
        Map<String, CommentEntity> commentEntityMap = commentEntities.stream().collect(Collectors.toMap(CommentEntity::getCommentId, e -> e));
        List<CommentEntity> replyEntities = comments.stream().filter(c -> c.getParentCommentId() != null).map(c -> CommentEntity.fromCommentDTO(c, videoEntityMap.get(c.getVideoId()), commentEntityMap.get(c.getParentCommentId()))).collect(Collectors.toList());

        // save entities
        SessionFactory factory = SessionFactorySource.getInstance().getSessionFactory();
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(channelEntity);
            videoEntities.forEach(session::saveOrUpdate);
            commentEntities.forEach(session::saveOrUpdate);
            replyEntities.forEach(session::saveOrUpdate);
            tx.commit();
        } catch (PersistenceException ex) {
            if (tx != null) tx.rollback();
        }
    }
}
