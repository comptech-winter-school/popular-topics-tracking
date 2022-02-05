package com.comptechschool.populartopicstracking.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtils {
    private final static String FILE_PATH = "src/main/resources/config.properties";
    private static final Properties properties = new Properties();

    static {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(FILE_PATH);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
