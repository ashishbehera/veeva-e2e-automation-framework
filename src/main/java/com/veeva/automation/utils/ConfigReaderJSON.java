package com.veeva.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veeva.automation.constants.FrameworkConstants;

import java.io.File;
import java.io.IOException;

/**
 * ConfigReaderJSON
 * ---------------------
 * Reads configuration data from config.json.
 * Mimics the API of ConfigReader so it can replace properties-based config.
 */
public class ConfigReaderJSON {

    private static JsonNode rootNode;
    private static final String JSON_CONFIG_FILE_PATH = FrameworkConstants.CONFIG_JSON_FILE_PATH;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(new File(JSON_CONFIG_FILE_PATH));
            System.out.println("✅ JSON Config loaded successfully from " + JSON_CONFIG_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load JSON config file: " + JSON_CONFIG_FILE_PATH, e);
        }
    }

    /**
     * Generic getter using dot notation for nested keys
     * Example: "browser.type" or "environment.urls.dp1"
     */
    public static String get(String key) {
        String[] nodes = key.split("\\.");
        JsonNode current = rootNode;
        for (String node : nodes) {
            current = current.path(node);
        }
        if (current.isMissingNode() || current.asText().trim().isEmpty()) {
            throw new RuntimeException("❌ Missing key in config.json: " + key);
        }
        return current.asText().trim();
    }

    /**
     * Optional helper to get integer values
     */
    public static int getIntValue(String jsonPointer) {
        return rootNode.at(jsonPointer).asInt();
    }

    /**
     * Optional helper to get boolean values
     */
    public static boolean getBooleanValue(String jsonPointer) {
        return rootNode.at(jsonPointer).asBoolean();
    }

    /**
     * Optional helper to print all loaded configurations (for debugging)
     */
    public static void printAll() {
        System.out.println("\n========== Loaded JSON Configurations ==========");
        System.out.println(rootNode.toPrettyString());
        System.out.println("=================================================\n");
    }
}
