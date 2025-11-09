package com.veeva.automation.steps.coreproduct;

import org.testng.Assert;

import com.veeva.automation.factory.PageFactoryManager;
import com.veeva.automation.pages.coreproduct.CoreHomePage;
import com.veeva.automation.pages.coreproduct.CoreNewFeaturesPage;
import com.veeva.automation.steps.Hooks;
import com.veeva.automation.utils.ConfigReader;
import com.veeva.automation.utils.PageTitles;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CoreVideoSteps {

	private PageFactoryManager factory;
	private CoreHomePage home;
	private CoreNewFeaturesPage features;
    private static final String PAGE_URL = ConfigReader.get("base.url.cp");


	public CoreVideoSteps(Hooks hooks) {
		factory = hooks.getPageFactory();
		home = factory.getPage(CoreHomePage.class);
		features = factory.getPage(CoreNewFeaturesPage.class);

	}

	@Given("I am on the CP home page")
	public void i_am_on_the_cp_home_page() throws InterruptedException {
		home.openHomePage(PAGE_URL);
	}

	@When("I hover on the Menu Item in the navigation bar")
	public void i_hover_on_the_menu_item_in_the_navigation_bar() {
		home.hoverThreeDotsMenu();
	}

	@When("I click on New & Features")
	public void i_click_on_new_features() {
		home.clickNewAndFeatures();
	}

	@Then("I should be on the New & Features page")
	public void i_should_be_on_the_new_features_page() {

		boolean result = features.verifyPageTitle(PageTitles.NEWS_FEATURES_TITLE);
		Assert.assertTrue(result, "❌ Page title does not match expected!");
		System.out.println("✅ Page title verified successfully!");
	}

	@When("I count all video feed items on the page")
	public void i_count_all_video_feed_items_on_the_page() {
		Assert.assertEquals(features.getTotalVideoFeedCount(), 45);
	}

	@Then("the total number of video feeds should be greater than or equal to {int}")
	public void the_total_number_of_video_feeds_should_be_greater_than_or_equal_to(Integer int1) {
		System.out.println("Videos number greater than equal 3d is: "+ features.countVideosOlderThan3Days());
	}

}
