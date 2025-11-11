//package com.veeva.automation.runner;
//
//import org.testng.annotations.DataProvider;
//import io.cucumber.testng.AbstractTestNGCucumberTests;
//import io.cucumber.testng.CucumberOptions;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.BeforeClass;
//
///**
// * Enterprise-grade Cucumber + TestNG runner for Veeva Automation Framework.
// * Enables reporting, parallel execution, and selective tag-based runs.
// */
//@CucumberOptions(features = { "src/test/resources/features" }, glue = { "com.veeva.automation.steps",
//		"com.veeva.automation.hooks" }, tags = "@Smoke or @Regression", // ✅ Or parameterize using Maven (see below)
//		monochrome = true, dryRun = false, // ✅ Should be false for execution
//		plugin = { "pretty", "html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json",
//				"junit:target/cucumber-reports/cucumber.xml", "timeline:target/test-output-thread/",
//				"rerun:target/failed_scenarios.txt" // ✅ For rerun support
//		}, publish = false // Disable automatic cloud publishing (optional)
//)
//public class TestNGTestRunner extends AbstractTestNGCucumberTests {
//
//	@Parameters("browser")
//	@BeforeClass(alwaysRun = true)
//	public void setUpBrowser(String browser) {
//		System.setProperty("browser", browser);
//	}
//
//	@Override
//	@DataProvider(parallel = true) // ✅ Keep parallel enabled for performance
//	public Object[][] scenarios() {
//		return super.scenarios();
//	}
//}

package com.veeva.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;

import com.veeva.automation.base.DriverManager;
import com.veeva.automation.utils.ConfigReaderJSON;

/**
 * UNIVERSAL TestNG Runner
 * -----------------------------------------------------
 * ✅ Supports:
 * 1. Parallel execution
 * 2. Multi-browser runs
 * 3. Dynamic tag binding (from TestNG XML or Maven)
 * 4. Default fallbacks when no parameters given
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.veeva.automation.steps"},
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"

        }
)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

	private String FILTER_TAGS_KEY="cucumber.filter.tags";
	private String DEAFAULT_BROWSER="chrome";
	public  String CUCUMBER_REPORTS_FOLDER = "target/cucumber-reports/";

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "cucumber.filter.tags"})
    public void setup(@Optional String xmlBrowser, @Optional String xmlTags) {

        // ----------------------------
        // Step 1: Resolve Browser
        // ----------------------------
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) browser = xmlBrowser;
        if (browser == null || browser.isEmpty()) browser = ConfigReaderJSON.get("browser.type"); // fallback
        if (browser == null || browser.isEmpty()) browser = DEAFAULT_BROWSER; // final fallback
        System.setProperty("browser", browser);

        // ----------------------------
        // Step 2: Resolve Tags
        // ----------------------------
        String tags = System.getProperty(FILTER_TAGS_KEY);
        if (tags == null || tags.isEmpty()) tags = xmlTags;
        if (tags == null || tags.isEmpty()) {
            System.out.println("⚠️ No tags provided, executing all feature files.");
            System.clearProperty("cucumber.filter.tags");
        } else {
            System.setProperty("cucumber.filter.tags", tags);
        }

        // ----------------------------
        // Step 3: Print Execution Context
        // ----------------------------
        System.out.println("🧭 Execution Context:");
        System.out.println("   🌐 Browser: " + browser);
        System.out.println("   🏷️ Tags: " + (tags == null ? "All" : tags));
        System.out.println("   🧩 Runner Mode: " + (System.getProperty("testng.xml.path") != null ? "TestNG XML" : "Maven CLI"));

        // ✅ No WebDriver init here — Hooks handle browser startup per scenario
        
        String reportPath = CUCUMBER_REPORTS_FOLDER + browser;
        System.setProperty("cucumber.reporting.output.folder", reportPath);
    }

    // ----------------------------
    // Enable Parallel Scenario Execution
    // ----------------------------
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
