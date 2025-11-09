package com.veeva.automation.pages.derivedproduct2;

import com.veeva.automation.base.BasePage;
import com.veeva.automation.utils.LinkUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;

public class DP2FooterPage extends BasePage {

    @FindBy(css = "footer a[href]") // All links in footer
    private List<WebElement> footerLinks;

    public DP2FooterPage(WebDriver driver) {
        super(driver);
    }

    // Scroll to footer
    public void scrollToFooter() {
        scrollToBottom(); // Uses BasePage method
    }

    // Extract footer links
    public List<Map<String, String>> getFooterLinks() {
        return LinkUtils.extractLinks(footerLinks);
    }

    // Save footer links to CSV
    public void saveFooterLinksCSV(String filePath) {
        LinkUtils.saveLinksToCSV(getFooterLinks(), filePath);
    }

    // Find duplicate footer links
    public List<String> findDuplicateFooterLinks() {
        return LinkUtils.findDuplicateLinks(getFooterLinks());
    }

    // Validate HTTP status of footer links
    public List<String> validateFooterLinks() {
        return LinkUtils.validateLinks(getFooterLinks());
    }
}
