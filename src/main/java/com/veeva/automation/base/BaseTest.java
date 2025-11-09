/*
 * package com.veeva.automation.base;
 * 
 * import com.veeva.automation.utils.ConfigReader; import
 * org.openqa.selenium.WebDriver;
 * 
 *//**
	 * BaseTest ------------------------ Centralized base class for driver setup and
	 * teardown. Delegates browser management to DriverManager and reads config
	 * values.
	 */
/*
 * public class BaseTest {
 * 
 * protected WebDriver driver;
 * 
 * 
 *//**
	 * Sets up the test environment before each scenario. Uses DriverManager to
	 * create driver instance.
	 */
/*
 * public void setUp() { DriverManager.initDriver(); // initializes ThreadLocal
 * driver driver = DriverManager.getDriver();
 * 
 * String appUrl = ConfigReader.get("base.url.cp"); driver.get(appUrl);
 * driver.manage().window().maximize();
 * 
 * System.out.println("🔧 Test setup completed — Navigated to: " + appUrl); }
 * 
 *//**
	 * Tears down the test environment after each scenario. Quits browser and cleans
	 * up ThreadLocal driver.
	 *//*
		 * public void tearDown() { DriverManager.quitDriver();
		 * System.out.println("🧹 Test teardown completed — Browser closed."); }
		 * 
		 * }
		 */