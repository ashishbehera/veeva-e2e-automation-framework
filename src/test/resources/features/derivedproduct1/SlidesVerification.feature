Feature: Validate Slide Carousel on DP1 (Sixers) Home Page

  @Smoke
  Scenario: Verify number of slides, their titles, and duration on Sixers homepage
    Given user navigates to the Sixers home page
    When user counts the total number of slides below the Tickets menu
    Then each slide title should match expected data from test file
