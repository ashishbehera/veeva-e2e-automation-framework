package com.veeva.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    
    // Method to get logger for any class
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    private LogUtils() {
        // Private constructor to prevent instantiation
    }
}
