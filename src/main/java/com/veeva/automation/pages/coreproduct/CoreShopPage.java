package com.veeva.automation.pages.coreproduct;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import com.veeva.automation.base.BasePage;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class CoreShopPage extends BasePage {

    private WebDriver driver;

    public CoreShopPage(WebDriver driver) {
    	super(driver);
        this.driver = driver;
    }


    @FindBy(xpath = "//span[contains(text(),'Jackets')]")
    private WebElement jacketsSubMenu;

    @FindBy(css = ".product-card")
    private List<WebElement> jacketsList;
    
    @FindBy(css = "a[aria-label='Men']")
    private WebElement mensMenu;
    

    
 

    // 🔹 Actions
    public void navigateToMensJackets() throws InterruptedException {
    	handleBlockingPopups();
    	waiForElementClick(mensMenu);
        jacketsSubMenu.click();
    }
}
