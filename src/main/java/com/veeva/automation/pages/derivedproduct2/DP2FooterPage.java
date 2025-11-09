package com.veeva.automation.pages.derivedproduct2;

import com.veeva.automation.base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * DP2FooterPage
 * -----------------
 * Handles footer link extraction and validation.
 */
public class DP2FooterPage extends BasePage {

	 private WebDriver driver;
    @FindBy(css = "footer a[href]") // all links in footer
    private List<WebElement> footerLinks;

    public DP2FooterPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    /**
     * Scrolls to footer area.
     */
    public void scrollToFooter() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Extracts footer link texts & hrefs.
     */
    public List<Map<String, String>> extractFooterLinks() {
        List<Map<String, String>> linkData = new ArrayList<>();

        for (WebElement link : footerLinks) {
            try {
                String text = link.getText().trim();
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    Map<String, String> data = new HashMap<>();
                    data.put("text", text.isEmpty() ? "(no text)" : text);
                    data.put("href", href);
                    linkData.add(data);
                }
            } catch (StaleElementReferenceException ignored) {
                // skip stale links
            }
        }
        System.out.println("✅ Total footer links extracted: " + linkData.size());
        return linkData;
    }

    /**
     * Saves link data to CSV file.
     */
    public void saveLinksToCSV(List<Map<String, String>> linkData, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Text,Hyperlink\n");
            for (Map<String, String> entry : linkData) {
                writer.write(String.format("\"%s\",\"%s\"\n", entry.get("text"), entry.get("href")));
            }
            System.out.println("📁 Footer link data saved to: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to write CSV file: " + e.getMessage());
        }
    }

    /**
     * Finds duplicate hyperlinks.
     */
    public List<String> findDuplicateLinks(List<Map<String, String>> linkData) {
        List<String> allLinks = linkData.stream()
                .map(l -> l.get("href"))
                .collect(Collectors.toList());

        Set<String> unique = new HashSet<>();
        return allLinks.stream()
                .filter(href -> !unique.add(href))
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * Validates all extracted footer links return 200 OK.
     * @param linkData List of link maps
     * @return List of broken URLs (non-200)
     */
  
    public List<String> validateFooterLinkStatus(List<Map<String, String>> linkData) {
        System.out.println("🌐 Validating HTTP responses for " + linkData.size() + " footer links...");

        // Thread pool — tune size based on CPU cores
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (Map<String, String> entry : linkData) {
            String href = entry.get("href");

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
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

                    if (statusCode != 200) {
                        return href + " [HTTP " + statusCode + "]";
                    }
                } catch (Exception e) {
                    return href + " [EXCEPTION: " + e.getMessage() + "]";
                }
                return null; // success = no error
            }, executor);

            futures.add(future);
        }

        // Collect results
        List<String> brokenLinks = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        executor.shutdown();

        if (brokenLinks.isEmpty()) {
            System.out.println("✅ All footer links returned HTTP 200 OK.");
        } else {
            System.out.println("❌ Broken links found:");
            brokenLinks.forEach(System.out::println);
        }

        return brokenLinks;
    }

}
