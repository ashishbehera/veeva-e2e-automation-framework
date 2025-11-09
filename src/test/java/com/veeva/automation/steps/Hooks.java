package com.veeva.automation.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

import com.veeva.automation.base.DriverManager;
import com.veeva.automation.factory.PageFactoryManager;
import com.veeva.automation.report.ExtentManager;
import com.veeva.automation.report.ExtentTestManager;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.veeva.automation.constants.HooksConstants;
import com.veeva.automation.constants.FrameworkConstants;


public class Hooks {

    private WebDriver driver;
    private PageFactoryManager pageFactoryManager;

    // -----------------------------
    // Before each scenario
    // -----------------------------
    @Before(order = 0)
    public void setupLogging() throws Exception {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String logPath = FrameworkConstants.LOGS_FOLDER + "/" + String.format(HooksConstants.LOG_FILE_PATTERN, timestamp);

        java.nio.file.Path dir = Paths.get(logPath).getParent();
        if (!Files.exists(dir)) Files.createDirectories(dir);
        System.setProperty("log.file.path", logPath);
        System.out.println("🧾 Log file: " + logPath);
    }

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {
        String browser = Reporter.getCurrentTestResult()
                .getTestContext()
                .getCurrentXmlTest()
                .getParameter("browser");

        if (browser == null || browser.isEmpty()) browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) browser = FrameworkConstants.DEFAULT_BROWSER;

        System.out.println("🚀 Launching browser: " + browser);
        DriverManager.initDriver(browser);
        driver = DriverManager.getDriver();
        pageFactoryManager = new PageFactoryManager(driver);

        String jsonPath = FrameworkConstants.CUCUMBER_JSON_FOLDER + "/" +
                          String.format(HooksConstants.CUCUMBER_JSON_PATTERN, browser);
        System.setProperty("cucumber.plugin.json", jsonPath);
        System.out.println("🧭 Cucumber JSON report path: " + jsonPath);

        ExtentTestManager.startTest(scenario.getName());
        ExtentTestManager.getTest().info("Scenario started on browser: " + browser);
    }

    @After(order = 0)
    public void attachJacketFile(Scenario scenario) throws Exception {
        if (scenario.getName().equalsIgnoreCase(HooksConstants.JACKET_SCENARIO_NAME)) {
            if (Files.exists(Paths.get(HooksConstants.JACKET_FILE))) {
                byte[] fileContent = Files.readAllBytes(Paths.get(HooksConstants.JACKET_FILE));
                scenario.attach(fileContent, "text/plain", "JacketData.txt");
            } else {
                scenario.log("JacketData.txt not found – nothing to attach.");
            }
        }
    }
 // -----------------------------
    // After each scenario
    // -----------------------------
    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        // Log pass/fail to Extent
        if (scenario.isFailed()) {
            ExtentTestManager.getTest().fail("Scenario Failed");

            // Capture screenshot
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                ExtentTestManager.getTest().addScreenCaptureFromBase64String(
                        java.util.Base64.getEncoder().encodeToString(screenshot),
                        "Failed Screenshot"
                );
            }

        } else {
            ExtentTestManager.getTest().pass("Scenario Passed");
        }

        // Close browser
        System.out.println("🧹 Closing browser after scenario...");
        DriverManager.quitDriver();
        System.out.println("✅ Browser closed.");

        // Flush report
        ExtentTestManager.endTest();
    }

    // Getter for PageFactory
    public PageFactoryManager getPageFactory() {
        return pageFactoryManager;
    }

    // Getter for WebDriver
    public WebDriver getDriver() {
        return driver;
    }
}
