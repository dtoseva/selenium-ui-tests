package com.ui.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigEnv {
    private static Properties properties = new Properties();

    public static void loadEnv(String environment) {

        try {
            FileInputStream file = new FileInputStream("src/test/resources/" + environment + ".properties");
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment: " +  environment,e);
        }       

    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    
}
