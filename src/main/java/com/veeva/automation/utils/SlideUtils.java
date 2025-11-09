package com.veeva.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class SlideUtils {

    private static final int TOLERANCE_SECONDS = 2; // ±2 seconds tolerance

    // Convert string "HH:mm:ss" -> Duration
    public static Duration parseDuration(String durationStr) {
        if (durationStr == null || durationStr.isEmpty()) return null;
        String[] parts = durationStr.split(":");
        if (parts.length != 3) return null;
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    // Format Duration back to "HH:mm:ss" string
    public static String formatDuration(Duration duration) {
        if (duration == null) return "00:00:00";
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Read expected slides from JSON -> Map<title, Duration>
    public static Map<String, Duration> readExpectedSlides(File jsonFile) throws IOException {
        Map<String, Duration> expectedMap = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode slidesArray = mapper.readTree(jsonFile).get("slides");

        for (JsonNode node : slidesArray) {
            String title = node.get("title").asText();
            Duration duration = node.has("expectedDuration") ?
                    parseDuration(node.get("expectedDuration").asText()) : null;
            expectedMap.put(title, duration);
        }

        return expectedMap;
    }

    // Validate UI Map against Expected Map
    public static void validateSlides(Map<String, Duration> uiMap, Map<String, Duration> expectedMap) {
        for (Map.Entry<String, Duration> entry : uiMap.entrySet()) {
            String title = entry.getKey();
            Duration uiDuration = entry.getValue();
            Duration expectedDuration = expectedMap.get(title);

            if (uiDuration != null && expectedDuration != null) {
                long diff = Math.abs(uiDuration.minus(expectedDuration).getSeconds());
                if (diff > TOLERANCE_SECONDS) {
                    System.out.println("❌ Duration mismatch for '" + title +
                            "'. UI: " + formatDuration(uiDuration) +
                            ", Expected: " + formatDuration(expectedDuration));
                } else {
                    System.out.println("✅ Duration matches for '" + title + "'");
                }
            } else if (uiDuration == null && expectedDuration != null) {
                System.out.println("⚠️ Slide '" + title + "' duration missing in UI. Expected: " +
                        formatDuration(expectedDuration));
            } else if (uiDuration != null && expectedDuration == null) {
                System.out.println("⚠️ Slide '" + title + "' duration exists in UI but missing in JSON: " +
                        formatDuration(uiDuration));
            } else {
                System.out.println("ℹ️ Slide '" + title + "' has no duration in UI or JSON, skipping validation.");
            }
        }
    }

    // Optional: Update JSON with UI durations
    public static void updateJsonWithUiDurations(File jsonFile, Map<String, Duration> uiMap) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile);
        for (JsonNode node : root.get("slides")) {
            String title = node.get("title").asText();
            Duration uiDuration = uiMap.get(title);
            if (uiDuration != null) {
                ((ObjectNode) node).put("expectedDuration", formatDuration(uiDuration));
            }
        }
        // Write updated JSON back
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, root);
    }
}
