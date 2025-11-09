package com.veeva.automation.pages.coreproduct;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.veeva.automation.base.BasePage;
import com.veeva.automation.utils.ConfigReader;

import io.cucumber.java.Scenario;

import org.openqa.selenium.WebElement;

public class CoreHomePage extends BasePage {

    private WebDriver driver;
   


    // Constructor
    public CoreHomePage(WebDriver driver) {
    	super(driver);
        this.driver = driver;
    }

    // 🔹 Locators
    @FindBy(xpath = "//a[contains(@href,'/shop')]")
    private WebElement shopMenu;

    @FindBy(css = "nav[id='nav-dropdown-desktop-1058447'] a[title='News & Features']")
    private WebElement newAndFeaturesMenu;
    

    @FindBy(xpath = "(//span[contains(text(),'Shop')])[1]")
    private WebElement shopsMenu;
    
    @FindBy(css = "li[data-testid='nav-item-']")
    private WebElement threeDotsMenu;
    
    @FindBy(xpath = "(//span[contains(text(),'Shop')])[1]")
    private WebElement shopLink;
    
 
   
    
    public void navigateToMens() {
    	clickShopAndSwitchToNewWindow(shopLink);
        System.out.println("🛒 Navigated to Shop.");
    }

    public void clickShopMenu() {
        shopMenu.click();
    }

    public void clickNewAndFeatures() {
        newAndFeaturesMenu.click();
        waitForPageToLoadCompletely();
    }
    
    public void hoverThreeDotsMenu() {
    	handleBlockingPopups();
        mouseOver(threeDotsMenu);
    }
    

}
