package com.veeva.automation.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.veeva.automation.constants.FrameworkConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {}

    public static ExtentReports getInstance() {
        if (extent == null) {
            // Build report path dynamically
        	String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        	String reportPath = FrameworkConstants.REPORTS_FOLDER 
        	                    + File.separator 
        	                    + "ExtentReport_" + timestamp + ".html";
            
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle(FrameworkConstants.EXTENT_REPORT_TITLE);
            spark.config().setReportName(FrameworkConstants.EXTENT_REPORT_NAME);

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}
