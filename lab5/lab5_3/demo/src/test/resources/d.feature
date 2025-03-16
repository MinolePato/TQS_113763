Feature: BlazeDemo

  Scenario: Flight Booking from Paris to Rome
    Given I navigate to "https://blazedemo.com/"
    When I select "Paris" for my departure City
    And I select "Rome" for my destination City
    And I click "Find Flights"
    Then I should be redirected to the page with title "BlazeDemo - reserve"
    When I click "Choose This Flight"
    Then I should be redirected to the page with title "BlazeDemo Purchase"
    And I enter my Name "Jaime"
    And I enter my Address "grove street"
    And I enter my City "lsos santos"
    And I enter my State "Murica"
    And I enter my Zip Code 1233
    And I enter my Credit Card Number 121212
    And I enter my Name on Card "Jaime"
    And I click "Purchase Flight"
    Then I should be redirected to the page with title "BlazeDemo Confirmation"