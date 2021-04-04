package com.ctzn.youtubescraper.addons.hibernateconnector.sessionfactory;

import com.ctzn.youtubescraper.addons.hibernateconnector.entity.ChannelEntity;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.CommentEntity;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.VideoEntity;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.WorkerLogEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Properties;

public class AbstractSessionFactory {

    private static class SingletonHolder {
        private final static SessionFactory SESSION_FACTORY = newInstance();
    }

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
        return SingletonHolder.SESSION_FACTORY;
    }

}
