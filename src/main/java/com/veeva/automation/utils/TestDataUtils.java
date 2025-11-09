package com.veeva.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDataUtils {
	
    private static final String SELECTORS_JSON = "src/test/resources/testdata/selectors.json";
    private static JsonObject selectors;

    public static List<String> getExpectedSlideTitles(String filePath) {
        List<String> titles = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode slides = root.path("slides");

            for (JsonNode node : slides) {
                titles.add(node.path("expectedTitle").asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }
    
 // Lazy load selectors
    private static void loadSelectors() {
        if (selectors == null) {
            try (FileReader reader = new FileReader(SELECTORS_JSON)) {
                selectors = new Gson().fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load selectors.json", e);
            }
        }
    }

    /**
     * Get all selectors for a page as a Map<String, String>
     */
    public static Map<String, String> getSelectors(String pageName) {
        loadSelectors(); // ensure JSON loaded
        if (!selectors.has(pageName)) {
            throw new RuntimeException("No selectors found for page: " + pageName);
        }
        return new Gson().fromJson(selectors.get(pageName), Map.class);
    }

    /**
     * Get single selector string by pageName and key
     */
    public static String getSelector(String pageName, String key) {
        Map<String, String> pageSelectors = getSelectors(pageName);
        if (!pageSelectors.containsKey(key)) {
            throw new RuntimeException("Selector '" + key + "' not found for page: " + pageName);
        }
        return pageSelectors.get(key);
    }
}

