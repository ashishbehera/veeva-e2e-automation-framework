package com.veeva.automation.pages.coreproduct;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.veeva.automation.base.BasePage;
import com.veeva.automation.utils.LogUtils;

import org.openqa.selenium.WebElement;

public class CoreHomePage extends BasePage {

   
    // 🔹 Logger
    private static final Logger log = LogUtils.getLogger(CoreHomePage.class);

    // Constructor
    public CoreHomePage(WebDriver driver) {
    	super(driver);
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
    	 log.info("🛒 Navigated to Shop.");
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
