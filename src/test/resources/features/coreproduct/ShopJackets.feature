Feature: Shop Jackets in Warriors Store

  @Smoke @Test		
  Scenario: Validate Jacket Prices and Titles
    Given user is on Warriors Shop Men’s Jackets page
    When user extracts all jacket details
    Then jacket details should be saved in text file
