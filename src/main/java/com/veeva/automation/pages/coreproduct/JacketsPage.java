package com.veeva.automation.pages.coreproduct;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.veeva.automation.base.BasePage;
import com.veeva.automation.utils.FileUtils;
import com.veeva.automation.utils.LogUtils;
import com.veeva.automation.utils.TestDataUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class JacketsPage extends BasePage{

    private final WebDriver driver;
    private final WebDriverWait wait;
    // selectors map
    @FindBy(css = "div[class='pagination-component'] a[aria-label='next page']")
    private WebElement nextPage;

 
	// Define locators centrally or load from config
	private String productCard = ".product-card";
	private String title = ".product-card-title";
	private String price = ".price-card";
	private String badge = ".product-badges-container";
	private String pageAttribute = "aria-disabled";
	private String pageAttributeValue = "false";
    private static final String EXTRACT_PRODUCTS_JS = "src/test/resources/js/extractProducts.js";



    public JacketsPage(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
           
    }
    
    
    

    /**
     * Extracts all jacket details from current page using JS
     */
    public List<Map<String, String>> getAllJackets() {
 
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(productCard)));
        String jsCode = FileUtils.readJsFile(EXTRACT_PRODUCTS_JS);        
        JavascriptExecutor js = (JavascriptExecutor) driver;

        @SuppressWarnings("unchecked")
        List<Map<String, String>> products = (List<Map<String, String>>) js.executeScript(
                jsCode + " return extractProducts(document.querySelectorAll(arguments[0]), arguments[1], arguments[2], arguments[3]);",
                productCard, title, price, badge
        );

        return products;
    }

    /**
     * Navigate to next page if exists
     */
    public boolean goToNextPageIfExists() {
        try {
            if (nextPage.getAttribute(pageAttribute).equalsIgnoreCase(pageAttributeValue)) {
                nextPage.click();
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(productCard)));
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

  
}
