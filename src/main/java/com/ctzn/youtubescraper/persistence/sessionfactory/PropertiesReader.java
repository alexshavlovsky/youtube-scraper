package com.ctzn.youtubescraper.persistence.sessionfactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    static final String DEFAULT_CONFIG_PATH = "src/main/resources/config.properties";
    private final String path;

    public PropertiesReader() {
        this(DEFAULT_CONFIG_PATH);
    }

    public PropertiesReader(String path) {
        this.path = path;
    }

    Properties getProperties() {
        try (InputStream input = new FileInputStream(path)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new Properties();
    }

}
