package com.veeva.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "<FEATURE_PATH>",
    glue = {"com.veeva.automation.steps"},
    plugin = {
        "pretty",
        "json:target/cucumber-reports/cucumber-<BROWSER>.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true
)
public class <RUNNER_NAME> extends AbstractTestNGCucumberTests {
}
