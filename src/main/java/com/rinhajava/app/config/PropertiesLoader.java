package com.rinhajava.app.config;

import java.io.InputStream;
import java.util.Properties;

import com.rinhajava.app.Main;

public class PropertiesLoader {
    private static Properties properties;

    private PropertiesLoader() {}

    public static void loadProperties(String filePath) {
        properties = new Properties();
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream(filePath);
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if(properties == null) {
            throw new IllegalStateException("Properties not loaded. Call loadProperties() first");
        }
        return properties.getProperty(key);
    }
}
