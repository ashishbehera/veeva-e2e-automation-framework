package com.veeva.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "<FEATURE_PATH>",
    glue = "com.veeva.automation.steps",
    plugin = {"json:target/cucumber-reports/cucumber-<BROWSER>.json"}
)
public class <RUNNER_NAME> extends AbstractTestNGCucumberTests {
}
