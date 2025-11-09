package com.veeva.automation.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * PageFactoryManager
 * -------------------
 * Centralized factory class to initialize all Page Objects.
 * Helps decouple object creation from test logic and keeps page
 * initialization consistent across the framework.
 */
public class PageFactoryManager {

    private final WebDriver driver;

    // Constructor
    public PageFactoryManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Generic page initializer — initializes any page class passed to it.
     * @param pageClass The Page Object class to initialize
     * @return An instance of the requested page object
     */
    public <T> T getPage(Class<T> pageClass) {
        try {
            T pageInstance = pageClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
            PageFactory.initElements(driver, pageInstance);
            return pageInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize page: " + pageClass.getName(), e);
        }
    }
}
