package com.veeva.automation.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class LinkUtils {

	private static int STATUS_CODE=200;
    // Extract text & href from a list of WebElements
    public static List<Map<String, String>> extractLinks(List<WebElement> links) {
        List<Map<String, String>> linkData = new ArrayList<>();
        for (WebElement link : links) {
            try {
                String text = link.getText().trim();
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    Map<String, String> data = new HashMap<>();
                    data.put("text", text.isEmpty() ? "(no text)" : text);
                    data.put("href", href);
                    linkData.add(data);
                }
            } catch (StaleElementReferenceException ignored) {}
        }
        return linkData;
    }

    // Save links to CSV
    public static void saveLinksToCSV(List<Map<String, String>> linkData, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Text,Hyperlink\n");
            for (Map<String, String> entry : linkData) {
                writer.write(String.format("\"%s\",\"%s\"\n", entry.get("text"), entry.get("href")));
            }
            System.out.println("📁 Link data saved to: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to write CSV file: " + e.getMessage());
        }
    }

    // Find duplicate links
    public static List<String> findDuplicateLinks(List<Map<String, String>> linkData) {
        Set<String> unique = new HashSet<>();
        return linkData.stream()
                .map(l -> l.get("href"))
                .filter(href -> !unique.add(href))
                .distinct()
                .collect(Collectors.toList());
    }

    // Validate link HTTP status
    public static List<String> validateLinks(List<Map<String, String>> linkData) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (Map<String, String> entry : linkData) {
            String href = entry.get("href");
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    Response response = RestAssured
                            .given()
                            .relaxedHTTPSValidation()
                            .when()
                            .get(href)
                            .then()
                            .extract()
                            .response();
                    int statusCode = response.getStatusCode();
                    if (statusCode != STATUS_CODE) return href + " [HTTP " + statusCode + "]";
                } catch (Exception e) {
                    return href + " [EXCEPTION: " + e.getMessage() + "]";
                }
                return null;
            }, executor));
        }

        List<String> brokenLinks = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        executor.shutdown();
        return brokenLinks;
    }
}
