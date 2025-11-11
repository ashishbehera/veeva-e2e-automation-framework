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
    // JSON keys as constants
    private static final String SLIDES_KEY = "slides";
    private static final String EXPECTED_TITLE_KEY = "expectedTitle";

    public static List<String> getExpectedSlideTitles(String filePath) {
        List<String> titles = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode slides = root.path(SLIDES_KEY);

            for (JsonNode node : slides) {
                titles.add(node.path(EXPECTED_TITLE_KEY).asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }
    
}

