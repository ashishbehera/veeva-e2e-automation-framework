package com.veeva.automation.base;

import com.veeva.automation.utils.ConfigReaderJSON;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import static com.veeva.automation.constants.FrameworkConstants.*;

/**
 * DriverManager
 * -------------------
 * Thread-safe WebDriver manager for parallel execution.
 * Supports Chrome, Firefox, Edge, headless mode.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(String browserName) {
        if (driver.get() != null) return; // Already initialized

        String browser = (browserName != null && !browserName.isEmpty()) 
                ? browserName.toLowerCase() 
                : ConfigReaderJSON.get("browser.type").toLowerCase();

        boolean headless = ConfigReaderJSON.getBoolean("browser.headless");
        System.out.println("🚀 Running browser: " + browser + " | Headless: " + headless);

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless=new", "--disable-gpu");
                chromeOptions.addArguments(CHROME_COMMON_ARGS);
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                driver.set(new ChromeDriver(chromeOptions));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments(FIREFOX_COMMON_ARGS);
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                edgeOptions.addArguments("--window-size=" + BROWSER_WIDTH + "," + BROWSER_HEIGHT);
                driver.set(new EdgeDriver(edgeOptions));
                break;

            default:
                throw new RuntimeException("❌ Unsupported browser: " + browser);
        }

        driver.get().manage().window().setSize(new Dimension(BROWSER_WIDTH, BROWSER_HEIGHT));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = System.getProperty("browser");
            if (browser == null || browser.isEmpty()) {
                browser = ConfigReaderJSON.get("browser.type");
            }
            initDriver(browser);
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
