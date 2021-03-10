package com.ctzn.youtubescraper.persistence.sessionfactory;

import com.ctzn.youtubescraper.persistence.entity.ChannelEntity;
import com.ctzn.youtubescraper.persistence.entity.CommentEntity;
import com.ctzn.youtubescraper.persistence.entity.VideoEntity;
import com.ctzn.youtubescraper.persistence.entity.WorkerLogEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class H2DBSessionFactory {

    private final static SessionFactory instance = newInstance();

    private static SessionFactory newInstance() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate-postgres.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(ChannelEntity.class)
                .addAnnotatedClass(VideoEntity.class)
                .addAnnotatedClass(CommentEntity.class)
                .addAnnotatedClass(WorkerLogEntity.class)
                .getMetadataBuilder()
                .applyPhysicalNamingStrategy(new CustomPhysicalNamingStrategy())
                .build();

        return metadata.getSessionFactoryBuilder()
                .applyInterceptor(new TimeStampInterceptor())
                .build();
    }

    public static SessionFactory getInstance() {
        return instance;
    }
}
