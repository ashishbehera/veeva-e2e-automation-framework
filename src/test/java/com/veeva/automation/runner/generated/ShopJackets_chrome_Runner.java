package com.veeva.automation.runner.generated;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/coreproduct/ShopJackets.feature",
    glue = {"com.veeva.automation.steps"},
    plugin = {
        "pretty",
        "json:target/cucumber-reports/cucumber-chrome.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true
)
public class ShopJackets_chrome_Runner extends AbstractTestNGCucumberTests {
}
