package com.veeva.automation.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.veeva.automation.constants.FrameworkConstants;
import com.veeva.automation.utils.ConfigReaderJSON;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {}

    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (ExtentManager.class) {
                if (extent == null) {
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String reportPath = FrameworkConstants.REPORTS_FOLDER 
                                        + File.separator 
                                        + "ExtentReport_" + timestamp + ".html";
                    String title = ConfigReaderJSON.get("reporting.extentReport.title");
                    String name = ConfigReaderJSON.get("reporting.extentReport.name");

                    ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
                    spark.config().setDocumentTitle(title);
                    spark.config().setReportName(name);

                    extent = new ExtentReports();
                    extent.attachReporter(spark);
                }
            }
        }
        return extent;
    }

}
