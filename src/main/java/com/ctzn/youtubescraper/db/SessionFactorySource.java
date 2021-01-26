package com.ctzn.youtubescraper.db;

import com.ctzn.youtubescraper.entity.ChannelEntity;
import com.ctzn.youtubescraper.entity.CommentEntity;
import com.ctzn.youtubescraper.entity.VideoEntity;
import lombok.extern.java.Log;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

@Log
public class SessionFactorySource {

    private static final SessionFactorySource instance = new SessionFactorySource();
    private final SessionFactory sessionFactory;

    private SessionFactorySource() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(ChannelEntity.class)
                .addAnnotatedClass(VideoEntity.class)
                .addAnnotatedClass(CommentEntity.class)
                .getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    public static SessionFactorySource getInstance() {
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
