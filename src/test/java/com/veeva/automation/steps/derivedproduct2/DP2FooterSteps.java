package com.veeva.automation.steps.derivedproduct2;

import com.veeva.automation.factory.PageFactoryManager;
import com.veeva.automation.pages.derivedproduct2.*;
import com.veeva.automation.steps.Hooks;
import com.veeva.automation.utils.ConfigReader;

import io.cucumber.java.en.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DP2FooterSteps ----------------- Step definitions for Footer link extraction
 * and validation.
 */
public class DP2FooterSteps {

	private DP2FooterPage footerPage;
	private List<Map<String, String>> footerLinks;
	private PageFactoryManager factory;

	List<Map<String, String>> allProducts = new ArrayList<>();
	private static final String PAGE_URL = ConfigReader.get("base.url.dp2");

	public DP2FooterSteps(Hooks hooks) {
		factory = hooks.getPageFactory();
		footerPage = factory.getPage(DP2FooterPage.class);

	}

	@Given("user is on the DP2 home page")
	public void user_is_on_the_dp2_home_page() throws InterruptedException {
		footerPage.openHomePage(PAGE_URL);
	}

	@When("user scrolls to the footer section")
	public void user_scrolls_to_footer_section() {
		footerPage.scrollToFooter();
	}

	@When("user extracts all footer links into a CSV file")
	public void user_extracts_all_footer_links_into_csv() {
		footerLinks = footerPage.extractFooterLinks();
		footerPage.saveLinksToCSV(footerLinks, "target/FooterLinks.csv");
	}

	@Then("user should verify that no duplicate hyperlinks are present")
	public void user_should_verify_no_duplicate_links() {
		List<String> duplicates = footerPage.findDuplicateLinks(footerLinks);

		if (!duplicates.isEmpty()) {
			System.out.println("⚠️ Duplicate links found:");
			duplicates.forEach(System.out::println);
		} else {
			System.out.println("✅ No duplicate hyperlinks detected in footer.");
		}
		
		   // 🧩 REST API Validation for Footer Links
	         footerPage.validateFooterLinkStatus(footerLinks);

	}
}
