package com.veeva.automation.runner;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class RunnerSetup {

	@BeforeSuite
	@Parameters({"browser"})
	public void generateRunners(@Optional("") String browserFromXML) throws Exception {
	    // 1️⃣ Get browser(s) from CLI first
	    String browsersCLI = System.getProperty("browsers"); // e.g., chrome,firefox

	    // 2️⃣ Fallback to testng.xml if CLI not provided
	    String[] browsers;
	    if (browsersCLI != null && !browsersCLI.isEmpty()) {
	        browsers = browsersCLI.split(",");
	        System.out.println("🧭 Using browsers from CLI: " + String.join(",", browsers));
	    } else if (browserFromXML != null && !browserFromXML.isEmpty()) {
	        browsers = new String[]{browserFromXML};
	        System.out.println("🧭 Using browser from TestNG XML: " + browserFromXML);
	    } else {
	        browsers = new String[]{"chrome"}; // default
	        System.out.println("🧭 Using default browser: chrome");
	    }

	    // 3️⃣ Get tags (CLI > XML > default)
	    String tags = System.getProperty("cucumber.filter.tags");
	    if (tags == null || tags.isEmpty()) tags = System.getProperty("cucumber.tags");
	    if (tags == null || tags.isEmpty()) tags = "@Smoke";

	    // 4️⃣ Generate runners for each browser
	    for (String browser : browsers) {
	        DynamicRunnerGenerator.generateRunners(browser.trim(), tags);
	    }
	}

}
