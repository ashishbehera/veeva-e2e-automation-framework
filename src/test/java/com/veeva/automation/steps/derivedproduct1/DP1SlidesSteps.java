package com.veeva.automation.steps.derivedproduct1;

import com.veeva.automation.factory.PageFactoryManager;
import com.veeva.automation.pages.derivedproduct1.DP1SlidesPage;
import com.veeva.automation.pages.derivedproduct2.DP2FooterPage;
import com.veeva.automation.steps.Hooks;
import com.veeva.automation.utils.ConfigReader;
import com.veeva.automation.utils.TestDataUtils;

import io.cucumber.java.en.*;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class DP1SlidesSteps {

	private PageFactoryManager factory;
    private final DP1SlidesPage slidesPage;
    private List<String> actualTitlesData;
    private List<String> actualDurationSlideData;
	private static final String PAGE_URL = ConfigReader.get("base.url.dp1");
	private static final String SLIDES_TITLE_DATA = ConfigReader.get("slides.title.data");


	public DP1SlidesSteps(Hooks hooks) {
		factory = hooks.getPageFactory();
		slidesPage = factory.getPage(DP1SlidesPage.class);

	}
    @Given("user navigates to the Sixers home page")
    public void user_navigates_to_sixers_home_page() {
        slidesPage.openHomePage(PAGE_URL);
        slidesPage.waitForTicketsMenuToAppear();
    }

    @When("user counts the total number of slides below the Tickets menu")
    public void user_counts_total_slides() {
 
        System.out.println("🎢 Total Slides Found: " + slidesPage.countAllSlides());
    }

    @Then("each slide title should match expected data from test file")
    public void validate_slide_titles() {
    	
    	List<String> expectedTitlesData = TestDataUtils.getExpectedSlideTitles(
    			SLIDES_TITLE_DATA
    	);
    	System.out.println("expectedTitlesDatae: "+expectedTitlesData.size());

    	actualTitlesData = slidesPage.getAllSlideDetails();
    	System.out.println("actualTitlesData: "+actualTitlesData.size());

    	for (String expected : expectedTitlesData) {
    	    boolean matchFound = actualTitlesData.stream().anyMatch(title -> title.contains(expected));
    	    Assert.assertTrue(matchFound, "❌ Expected title not found: " + expected);
    	}

    	System.out.println("✅ All expected slide titles are present and validated.");
    	
    }

    
}
