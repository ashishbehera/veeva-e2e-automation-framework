package com.veeva.automation.pages.coreproduct;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.veeva.automation.base.BasePage;
import com.veeva.automation.utils.LogUtils;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CoreNewFeaturesPage extends BasePage {

    private WebDriver driver;
    
    // 🔹 Logger
    private static final Logger log = LogUtils.getLogger(CoreNewFeaturesPage.class);

    public CoreNewFeaturesPage(WebDriver driver) {
    	super(driver);
        this.driver = driver;
    }

    // 🔹 Locators
    @FindBy(xpath  = "//a[contains(@data-testid, 'tile-article-link') or contains(@data-testid, 'tile-featured-article-link')]")
    private List<WebElement> allVideoFeeds;
  
    
    @FindBy(css  = "time>span")
    private List<WebElement> videoAges;
    

    

    // 🔹 Actions
    public int getTotalVideoFeedCount() {
        log.info("Starting to get total video feed count on CoreNewFeaturesPage");
        AtomicInteger previousCount = new AtomicInteger(0);
        int currentCount = 0;
        int stableCountRepeats = 0;

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        while (stableCountRepeats < 3) {
            log.debug("Scrolling to bottom to load more videos...");
            js.executeScript("window.scrollBy(0, document.body.scrollHeight);");

            try {
                wait.until(d -> {
                    int newCount = allVideoFeeds.size();
                    log.debug("Current video feed size: {}", newCount);
                    return newCount > previousCount.get();
                });
            } catch (TimeoutException e) {
                // No new videos loaded during this scroll
                log.info("No new videos loaded during this scroll");

            }

            List<WebElement> allVideos = allVideoFeeds;
            currentCount = allVideos.size();
            log.debug("Total videos found after scroll: {}", currentCount);

            if (currentCount == previousCount.get()) {
                stableCountRepeats++;
                log.debug("Stable count repetition incremented to {}", stableCountRepeats);
            } else {
                stableCountRepeats = 0;
            }

            previousCount.set(currentCount);
        }
        log.info("Total video feed count: {}", currentCount);
        return currentCount;
    }



    public long getThreeDVideoCount() {
        return allVideoFeeds.stream()
                .filter(v -> v.getText().toLowerCase().contains("3d"))
                .count();
    }
    
    public long countVideosOlderThan3Days() {
    	return countByChildTextCondition(
    			videoAges,
    		    text -> text.contains("d") && Integer.parseInt(text.replace("d", "")) >= 3
    		);
		
    }
}


