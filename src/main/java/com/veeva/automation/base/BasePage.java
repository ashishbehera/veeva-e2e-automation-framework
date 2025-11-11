package com.veeva.automation.base;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.veeva.automation.pages.coreproduct.CoreHomePage;
import com.veeva.automation.utils.ConfigReaderJSON;
import com.veeva.automation.utils.LogUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Enhanced BasePage — Handles modals, cookies, and dynamic overlays.
 * All locators use @FindBy to avoid By selectors.
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    int explicitWait;

    // 🔹 Popups / Modals
    @FindBy(css = ".CustomModal_backdrop__TTyum")
    protected WebElement modalBackdrop;

    @FindBy(xpath = "//button[contains(text(),'Close') or @aria-label='Close']")
    protected WebElement closeModalBtn;

    @FindBy(xpath = "//button[contains(text(),'I Accept') or contains(text(),'I Decline')]")
    protected WebElement cookiesBtn;
    // 🔹 Logger
    private static final Logger log = LogUtils.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.explicitWait = ConfigReaderJSON.getIntValue("/browser/explicitWait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    /**
     * Handles any known blocking elements like cookie banners or modals.
     */
    protected void handleBlockingPopups() {
        try {
            // Cookies
            if (isElementVisible(cookiesBtn)) {
                log.info("🍪 Cookie banner detected. Closing...");
                cookiesBtn.click();
                wait.until(ExpectedConditions.invisibilityOf(cookiesBtn));
            }

            // Modal backdrop
            if (isElementVisible(modalBackdrop)) {
            	  log.info("🪟 Modal overlay detected.");
                try {
                    if (isElementVisible(closeModalBtn)) {
                        closeModalBtn.click();
                        System.out.println("✅ Modal closed via button.");
                    } else {
                        ((JavascriptExecutor) driver)
                                .executeScript("arguments[0].click();", modalBackdrop);
                        System.out.println("✅ Modal closed via JS backdrop click.");
                    }
                    wait.until(ExpectedConditions.invisibilityOf(modalBackdrop));
                } catch (Exception ignored) {
                }
            }      
        } catch (Exception e) {
        	  log.info("ℹ️ No blocking popups detected: " + e.getMessage());
        }
    }

    /**
     * Safe click method with popup handling.
     */
    public void safeClick(WebElement element) {
        handleBlockingPopups();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
        	  log.info("⚠️ Click intercepted — retrying after handling popups...");
            handleBlockingPopups();
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        }
    }

    public void clickShopAndSwitchToNewWindow(WebElement element) {
        handleBlockingPopups();
        try {
            String currentWindow = driver.getWindowHandle();
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            log.info("🛒 Clicked on Shop link, waiting for new window...");

            wait.until(d -> d.getWindowHandles().size() > 1);

            Set<String> allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!window.equals(currentWindow)) {
                    driver.switchTo().window(window);
                    System.out.println("🔄 Switched to new window: " + driver.getTitle());
                    break;
                }
            }

            waitForPageToLoadCompletely();
        } catch (ElementClickInterceptedException e) {
        	  log.info("⚠️ Click intercepted — retrying after handling popups...");
            handleBlockingPopups();
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        }
    }

    public void waiForElementClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
        	  log.info("⚠️ Click intercepted — retrying after handling popups...");
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        }
    }

    public void mouseOver(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean verifyPageTitle(String expectedTitle) {
        return getPageTitle().equalsIgnoreCase(expectedTitle);
    }

    protected long countByChildTextCondition(List<WebElement> childElements, Predicate<String> condition) {
        List<WebElement> snapshot = new ArrayList<>(childElements);
        return snapshot.parallelStream()
                .map(child -> {
                    try {
                        return child.getText().trim();
                    } catch (NoSuchElementException | StaleElementReferenceException e) {
                        return "";
                    }
                })
                .filter(text -> !text.isEmpty() && condition.test(text))
                .count();
    }

    public void waitForPageToLoad() {
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    public void waitForPageToLoadCompletely() {
        waitForPageToLoad();
        wait.until(d -> {
            try {
                return (Boolean) ((JavascriptExecutor) d)
                        .executeScript("return typeof jQuery === 'undefined' || jQuery.active === 0");
            } catch (Exception e) {
                return true;
            }
        });
    }


    public void openHomePage(String url) {
        driver.get(url);
        log.info("🌐 Navigated to the desired page with Page URL: " + url);
    }

    /**
     * Short visibility check for WebElement
     */
    protected boolean isElementVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected int countElements(List<WebElement> elements) {
        return elements.size();
    }

    // Generic scroll to bottom
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected List<WebElement> getElements(WebElement parent, String cssSelector) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object result = js.executeScript(
                    "return arguments[0].querySelectorAll(arguments[1]);", parent, cssSelector);

            if (result instanceof List<?>) {
                List<?> rawList = (List<?>) result;
                List<WebElement> elements = new ArrayList<>();
                for (Object item : rawList) {
                    if (item instanceof WebElement) {
                        elements.add((WebElement) item);
                    }
                }
                return elements;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

}
