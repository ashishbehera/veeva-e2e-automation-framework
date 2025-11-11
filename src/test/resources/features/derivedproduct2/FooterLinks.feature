Feature: Validate Footer Links on DP2 Home Page
  As a user of DP2 website
  I want to verify all footer hyperlinks are unique
  So that there are no duplicate links present

  @Smoke
  Scenario: Extract and validate footer hyperlinks
    Given user is on the DP2 home page
    When user scrolls to the footer section
    And user extracts all footer links into a CSV file
    Then user should verify that no duplicate hyperlinks are present
