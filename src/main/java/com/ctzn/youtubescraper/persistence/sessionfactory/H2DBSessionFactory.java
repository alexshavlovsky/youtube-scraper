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

import java.util.Properties;

public class H2DBSessionFactory {

    private final static SessionFactory instance = newInstance();
    private final static String HIBERNATE_CFG_FILE_KEY = "hibernate.configuration.xml";

    private static SessionFactory newInstance() {
        Properties properties = new PropertiesReader().getProperties();
        String cfgFile = properties.getProperty(HIBERNATE_CFG_FILE_KEY);
        if (cfgFile == null) throw new RuntimeException(
                String.format("Please specify '%s=???' property in 'config.properties' file", HIBERNATE_CFG_FILE_KEY));

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure(cfgFile)
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
