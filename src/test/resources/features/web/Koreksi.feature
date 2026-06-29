@web @regression @koreksi
Feature: Laporan Koreksi Page Testing

  Background:
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard

  Scenario: Search by User Name (Positive)
    And User is logged in and on the Laporan Koreksi page
    When User enters search keyword "HST" in Koreksi page
    And User clicks search button in Koreksi page
    Then The table should show Koreksi records matching "HST"

  Scenario: Reset Filter (Positive)
    And User is logged in and on the Laporan Koreksi page
    When User enters search keyword "HST" in Koreksi page
    And User clicks reset button in Koreksi page
    Then The search field in Koreksi page should be empty

  Scenario: Search by Date Range (Positive)
    And User is logged in and on the Laporan Koreksi page
    When User enters start date "06/01/2026" and end date "06/30/2026" in Koreksi page
    And User clicks search button in Koreksi page
    Then The table should show Koreksi records filtered by dates

  Scenario: Pagination Navigation (Positive)
    And User is logged in and on the Laporan Koreksi page
    And User stores initial pagination state in Koreksi page
    When User clicks next page button in Koreksi page
    Then The table should show the next page of Koreksi records
    When User clicks previous page button in Koreksi page
    Then The table should show the previous page of Koreksi records

  Scenario: Change rows per page (Positive)
    And User is logged in and on the Laporan Koreksi page
    When User changes rows per page to "25" in Koreksi page
    Then The table should show at most 25 Koreksi records

  @negative
  Scenario: Direct access to Laporan Koreksi without login (Negative)
    Given User attempts to navigate to Laporan Koreksi page directly without logging in
    Then User is redirected back to the login page on Koreksi page check
