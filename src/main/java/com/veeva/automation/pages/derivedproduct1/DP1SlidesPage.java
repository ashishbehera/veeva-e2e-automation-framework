package com.veeva.automation.pages.derivedproduct1;

import com.veeva.automation.base.BasePage;
import com.veeva.automation.pages.coreproduct.CoreHomePage;
import com.veeva.automation.utils.ConfigReaderJSON;
import com.veeva.automation.utils.LogUtils;

import static com.veeva.automation.constants.FrameworkConstants.*;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DP1SlidesPage extends BasePage {

    // Main slide elements
    @FindBy(css = "div[class*='CourtsideBox_courtsideBoxItemMain']")
    private List<WebElement> slides;

    @FindBy(css = "li[data-testid$='nav-item-/sixers/tickets']")
    private WebElement ticketMenuElement;
    int explicitWait;
 
 

    // Class-level selectors as Strings
    private  String teamSelector="span[class*='Game_gameTeam'], div[class*='Game_featuredGameTeam'] p[class*='text-sm']";
    private  String statusSelector="span[data-testid$='status-indicator']";
    private int count=3;
    private String regex = "\\s*\\(\\d+\\s*-\\s*\\d+\\)\\s*";
    private String splitWord = "\\s+";
	   // 🔹 Logger
   private static final Logger log = LogUtils.getLogger(DP1SlidesPage.class);

    public DP1SlidesPage(WebDriver driver) {
        super(driver);
        this.explicitWait = ConfigReaderJSON.getIntValue("/browser/explicitWait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }
    
    public void waitForTicketsMenuToAppear() {  	
     handleBlockingPopups();	
     isElementVisible(ticketMenuElement);
    }

    public int countAllSlides() {
        return countElements(slides);
    }

    public List<String> getAllSlideDetails() {
        List<String> slideSummaries = new ArrayList<>();
        log.info("🎞 Total Slides Found: " + slides.size());
        
        for (WebElement slide : slides) {
            try {
                List<WebElement> teamDetails = getElements(slide, teamSelector);
                List<WebElement> statusDetails = getElements(slide, statusSelector);
                StringBuilder rawInfo = new StringBuilder();

                for (WebElement detail : teamDetails) {
                	String text = null;
                	if(teamDetails.size() <= count) text = detail.getAttribute("innerText").trim();
                	if(teamDetails.size() > count) text = detail.getText().trim();
                    if (!text.isEmpty() && rawInfo.indexOf(text) == -1) {
                        rawInfo.append(text).append(" ");
                    }
                }

                for (WebElement status : statusDetails) {
                    String text = status.getAttribute("innerText").trim();
                    if (!text.isEmpty()) {
                        rawInfo.append(text).append(" ");
                    }
                }

                if (rawInfo.length() > 1) {
                    String formatted = formatSlideDetailsNew(rawInfo.toString());
                    slideSummaries.add(formatted);
                }

            } catch (Exception e) {
                log.error("⚠️ Error extracting slide details: " + e.getMessage());
            }
        }

        return slideSummaries;
    }



    private String formatSlideDetailsNew(String rawInfo) {
        String cleaned = rawInfo.replaceAll(regex, " ").trim();
        String[] parts = cleaned.split(splitWord);
        if (parts.length == count) {
            return String.format("%s VS %s - %s", parts[0], parts[1], parts[2]);
        }
        return null;
    }
}
