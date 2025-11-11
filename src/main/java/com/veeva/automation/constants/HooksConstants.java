package com.veeva.automation.constants;

public final class HooksConstants {

    private HooksConstants() {} // Prevent instantiation

    // 🔹 Log file pattern
    public static final String LOG_FILE_PATTERN = "veeva-automation-%s.log"; // timestamp placeholder

    // 🔹 Cucumber JSON report pattern
    public static final String CUCUMBER_JSON_PATTERN = "cucumber-%s.json"; // browser placeholder

    // Optional: Keep scenario-specific names as constants (can also move to JSON)
    public static final String JACKET_SCENARIO_NAME = "Validate Jacket Prices and Titles";
    public static final String JACKET_FILE = "target/JacketData.txt";
}
