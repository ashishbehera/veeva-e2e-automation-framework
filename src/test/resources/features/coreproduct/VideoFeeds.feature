Feature: New & Features Page Verification
  As a CP user
  I want to navigate to the New & Features page and verify video feeds
  So that I can ensure the content is displayed correctly

 @Smoke
  Scenario: Navigate to New & Features page and verify video feeds
    Given I am on the CP home page
    When I hover on the Menu Item in the navigation bar
    And I click on New & Features
    Then I should be on the New & Features page
    When I count all video feed items on the page
    Then the total number of video feeds should be greater than or equal to 3