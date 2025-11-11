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
 * DriverManager ------------------- Thread-safe WebDriver manager for parallel
 * execution. Supports Chrome, Firefox, Edge, headless mode.
 */
public class DriverManager {

	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static void initDriver(String browserName) {
		if (driver.get() != null) return; // Already initialized

	    // ✅ Read all config values once
	    String browserFromJson = ConfigReaderJSON.get("browser.type").toLowerCase();
	    String browser = (browserName != null && !browserName.isEmpty()) ? browserName.toLowerCase() : browserFromJson;

	    boolean headless = System.getProperty("browser.headless") != null
	            ? Boolean.parseBoolean(System.getProperty("browser.headless"))
	            : ConfigReaderJSON.getBooleanValue("/browser/headless");

	    int width = ConfigReaderJSON.getIntValue("/browser/width");
	    int height = ConfigReaderJSON.getIntValue("/browser/height");
	    boolean maximize = ConfigReaderJSON.getBooleanValue("/browser/maximizeWindow");
	    int implicitWait = ConfigReaderJSON.getIntValue("/browser/implicitWait");

	    System.out.println("🚀 Running browser: " + browser + " | Headless: " + headless);

	    // ✅ Initialize browser
	    switch (browser) {
	        case "chrome" : {
	            WebDriverManager.chromedriver().setup();
	            ChromeOptions options = new ChromeOptions();
	            if (headless) options.addArguments("--headless=new", "--disable-gpu");
	            options.addArguments(CHROME_COMMON_ARGS);
	            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
	            options.setExperimentalOption("useAutomationExtension", false);
	            driver.set(new ChromeDriver(options));
	            break;
	        }

	        case "firefox": {
	            WebDriverManager.firefoxdriver().setup();
	            FirefoxOptions options = new FirefoxOptions();
	            if (headless) options.addArguments("--headless");
	            options.addArguments(FIREFOX_COMMON_ARGS);
	            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
	            driver.set(new FirefoxDriver(options));
	            break;
	        }

	        case "edge": {
	            WebDriverManager.edgedriver().setup();
	            EdgeOptions options = new EdgeOptions();
	            if (headless) options.addArguments("--headless=new");
	            driver.set(new EdgeDriver(options));
	            break;
	        }

	        default:
	        	throw new RuntimeException("❌ Unsupported browser: " + browser);
	    }
	    // ✅ Window size
	    if (maximize) {
	        driver.get().manage().window().maximize();
	    } else {
	        driver.get().manage().window().setSize(new Dimension(width, height));
	    }

	    // ✅ Implicit wait
	    driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
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
