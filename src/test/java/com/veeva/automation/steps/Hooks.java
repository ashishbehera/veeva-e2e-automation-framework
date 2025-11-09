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

public class Hooks {

    private WebDriver driver;
    private PageFactoryManager pageFactoryManager;

    // -----------------------------
    // Before each scenario
    // -----------------------------
    @Before(order = 0)
    public void setupLogging() throws Exception {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String logPath = "logs/veeva-automation-" + timestamp + ".log";
        java.nio.file.Path dir = Paths.get(logPath).getParent();
        if (!Files.exists(dir)) Files.createDirectories(dir);
        System.setProperty("log.file.path", logPath);
        System.out.println("🧾 Log file: " + logPath);
    }

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {

        // --------------------------
        // Browser setup
        // --------------------------
        String browser = Reporter.getCurrentTestResult()
                .getTestContext()
                .getCurrentXmlTest()
                .getParameter("browser");

        if (browser == null || browser.isEmpty()) browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) browser = "chrome";

        System.out.println("🚀 Launching browser: " + browser);
        DriverManager.initDriver(browser);  // Thread-local safe
        driver = DriverManager.getDriver();
        pageFactoryManager = new PageFactoryManager(driver);

        // --------------------------
        // Cucumber JSON path (optional)
        // --------------------------
        String jsonPath = "target/cucumber-reports/cucumber-" + browser + ".json";
        System.setProperty("cucumber.plugin.json", jsonPath);

        System.out.println("🧭 Cucumber JSON report path: " + jsonPath);

        // --------------------------
        // Start ExtentTest
        // --------------------------
        ExtentTestManager.startTest(scenario.getName());
        ExtentTestManager.getTest().info("Scenario started on browser: " + browser);
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

    // -----------------------------
    // Attach file example
    // -----------------------------
    @After(order = 0)
    public void attachJacketFile(Scenario scenario) throws Exception {
        if (scenario.getName().equalsIgnoreCase("Validate Jacket Prices and Titles")) {
            String filePath = "target/JacketData.txt";
            if (Files.exists(Paths.get(filePath))) {
                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                scenario.attach(fileContent, "text/plain", "JacketData.txt");
            } else {
                scenario.log("JacketData.txt not found – nothing to attach.");
            }
        }
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
