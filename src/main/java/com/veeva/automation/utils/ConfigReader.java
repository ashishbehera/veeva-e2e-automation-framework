package com.veeva.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.veeva.automation.constants.FrameworkConstants;

/**
 * ConfigReader
 * ---------------------
 * Centralized utility to read configuration data from config.properties.
 * Ensures environment, browser, and framework settings are loaded once and
 * easily accessible across the project.
 */
public class ConfigReader {

    private static Properties properties;

    private static final String CONFIG_FILE_PATH = FrameworkConstants.getConfigFilePath();

    // Static block executes once when class is loaded
    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.getConfigFilePath())) {
            properties = new Properties();
            properties.load(fis);
            System.out.println("✅ Config loaded successfully from " + CONFIG_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config file: " + CONFIG_FILE_PATH, e);
        }
    }

    /**
     * Generic method to fetch property value.
     *
     * @param key - Key to be fetched from config.properties
     * @return value corresponding to key
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("❌ Missing key in config.properties: " + key);
        }
        return value.trim();
    }

    /**
     * Optional helper to get integer values
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Optional helper to get boolean values
     */
    public static boolean getBoolean(String key) {
    	 return Boolean.parseBoolean(properties.getProperty(key).trim());
    }

    /**
     * Optional helper to print all loaded configurations (for debugging)
     */
    public static void printAll() {
        System.out.println("\n========== Loaded Configurations ==========");
        properties.forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("===========================================\n");
    }
}
