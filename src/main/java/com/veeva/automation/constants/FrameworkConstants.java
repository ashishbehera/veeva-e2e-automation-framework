package com.veeva.automation.constants;

import java.nio.file.Paths;

public final class FrameworkConstants {

    private FrameworkConstants() {} // Prevent instantiation

    // 🔹 Path to global config JSON
    public static final String CONFIG_JSON_FILE_PATH = "src/main/resources/config/config.json";

    // 🔹 Browser CLI arguments (static, framework-level)
    public static final String[] CHROME_COMMON_ARGS = {
            "--disable-blink-features=AutomationControlled"
    };
    public static final String[] FIREFOX_COMMON_ARGS = {};

    // 🔹 Reporting folders (static base paths)
    public static final String REPORTS_FOLDER = Paths.get(System.getProperty("user.dir"), "target", "ExtentReports").toString();
    public static final String LOGS_FOLDER = "logs";
    public static final String CUCUMBER_JSON_FOLDER = "target/cucumber-reports";

    // 🔹 Page load strategy default (optional fallback)
    public static final String PAGE_LOAD_STRATEGY = "NORMAL";
}
