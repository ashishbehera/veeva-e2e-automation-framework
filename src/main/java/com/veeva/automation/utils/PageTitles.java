package com.veeva.automation.utils;

/**
 * PageTitles
 * -------------------
 * Centralized class for maintaining all expected page titles.
 * These values are dynamically fetched from config.properties.
 * 
 * Advantage:
 * - Single place to manage expected titles
 * - Avoids hardcoding in step definitions
 */
public class PageTitles {

    public static final String NEWS_FEATURES_TITLE = ConfigReader.get("news.features.page.title");

    private PageTitles() {
        // prevent instantiation
    }
}
