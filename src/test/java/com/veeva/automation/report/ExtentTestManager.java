package com.veeva.automation.report;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    public static void startTest(String testName) {
        ExtentTest test = ExtentManager.getInstance().createTest(testName);
        testThread.set(test);
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void endTest() {
        ExtentManager.getInstance().flush(); // Ensure flush is called
    }
}
