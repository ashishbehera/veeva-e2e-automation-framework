package com.veeva.automation.runner.generated;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/derivedproduct1/SlidesVerification.feature",
    glue = "com.veeva.automation.steps",
    plugin = {"json:target/cucumber-reports/cucumber-chrome.json"}
)
public class SlidesVerification_chrome_Runner extends AbstractTestNGCucumberTests {
}
