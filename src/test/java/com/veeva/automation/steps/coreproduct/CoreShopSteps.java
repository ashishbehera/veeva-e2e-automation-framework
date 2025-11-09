package com.veeva.automation.steps.coreproduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.veeva.automation.factory.PageFactoryManager;
import com.veeva.automation.pages.coreproduct.CoreHomePage;
import com.veeva.automation.pages.coreproduct.CoreShopPage;
import com.veeva.automation.pages.coreproduct.JacketsPage;
import com.veeva.automation.steps.Hooks;
import com.veeva.automation.utils.ConfigReader;
import com.veeva.automation.utils.JacketDataUtils;

import io.cucumber.java.en.*;

public class CoreShopSteps {

 
    private PageFactoryManager factory;
    private CoreHomePage home;
    private CoreShopPage shop;
    private JacketsPage jackets;
    List<Map<String, String>> allProducts = new ArrayList<>();
    private static final String PAGE_URL = ConfigReader.get("base.url.cp");



    public CoreShopSteps(Hooks hooks) {
        factory = hooks.getPageFactory();
        home = factory.getPage(CoreHomePage.class);
        shop = factory.getPage(CoreShopPage.class);
        jackets = factory.getPage(JacketsPage.class);
    }
    

@Given("user is on Warriors Shop Men’s Jackets page")
public void user_is_on_warriors_shop_men_s_jackets_page() throws InterruptedException {
    home.openHomePage(PAGE_URL);
    home.navigateToMens();
    shop.navigateToMensJackets();
}
@When("user extracts all jacket details")
public void user_extracts_all_jacket_details() throws IOException {
	
	  do {
	        allProducts.addAll(jackets.getAllJackets());
	    	System.out.println("I am inside storing product details in TS");
	    } while (jackets.goToNextPageIfExists());
	  
	 

}
@Then("jacket details should be saved in text file")
public void jacket_details_should_be_saved_in_text_file() throws IOException {
	JacketDataUtils.saveJacketData(allProducts);
}
}
