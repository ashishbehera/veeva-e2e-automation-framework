package com.veeva.automation.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.veeva.automation.constants.FrameworkConstants;

import java.io.File;

public class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {}

    public static ExtentReports getInstance() {
        if (extent == null) {
            // Build report path dynamically
            String reportPath = FrameworkConstants.REPORTS_FOLDER + File.separator + FrameworkConstants.EXTENT_REPORT_FILE;
            
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle(FrameworkConstants.EXTENT_REPORT_TITLE);
            spark.config().setReportName(FrameworkConstants.EXTENT_REPORT_NAME);

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}
