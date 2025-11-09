package com.veeva.automation.constants;

public class FrameworkConstants {
	
	private FrameworkConstants() {} // Prevent instantiation
	
	   private static final String CONFIG_FILE_PATH = "src/main/resources/config/config.properties";

	    public static String getConfigFilePath() {
	        return CONFIG_FILE_PATH;
	    }

    // 🔹 Browser defaults
    public static final String DEFAULT_BROWSER = "chrome";
    public static final boolean DEFAULT_HEADLESS = false;

    // 🔹 Window dimensions
    public static final int BROWSER_WIDTH = 1920;
    public static final int BROWSER_HEIGHT = 1080;

    // 🔹 Timeouts
    public static final int IMPLICIT_WAIT_SECONDS = 20;

    // 🔹 Page load strategy
    public static final String PAGE_LOAD_STRATEGY = "NORMAL";  // could also come from config.properties

    // 🔹 Chrome/Firefox CLI arguments
    public static final String[] CHROME_COMMON_ARGS = {
        "--window-size=1920,1080",
        "--disable-blink-features=AutomationControlled"
    };
    public static final String[] FIREFOX_COMMON_ARGS = {
        "--width=1920",
        "--height=1080"
    };
    
    // ========= WAIT TIMES =========
    public static final int DEFAULT_WAIT = 10;
    public static final int LONG_WAIT = 30;
    
    // ========= URLs =========
    public static final String SIXERS_URL = "https://www.nba.com/sixers/";
    
    // ========= Extent Report =========
    public static final String REPORTS_FOLDER = System.getProperty("user.dir") + "/target/ExtentReports";
    public static final String EXTENT_REPORT_FILE = "ExtentReport.html"; // default
    public static final String EXTENT_REPORT_TITLE = "Veeva Automation Report";
    public static final String EXTENT_REPORT_NAME = "E2E Test Results";
     public static final String LOGS_FOLDER = "logs";
    public static final String CUCUMBER_JSON_FOLDER = "target/cucumber-reports";
}
