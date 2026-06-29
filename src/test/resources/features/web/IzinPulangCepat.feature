@web @regression @izin-pulang-cepat
Feature: Izin Pulang Cepat Report Testing

  Background:
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard
    And User is logged in and on the Izin Pulang Cepat page

  Scenario: Search by User Name
    When User enters search keyword "HST"
    And User clicks search button
    Then The table should show records matching "HST"

  Scenario: Reset Filter
    When User enters search keyword "HST"
    And User clicks reset button
    Then The search field should be empty

  Scenario: Search by Date Range
    When User enters start date "06/01/2026" and end date "06/30/2026"
    And User clicks search button
    Then The table should show records filtered by dates

  Scenario: Pagination Navigation
    And User stores initial pagination state
    When User clicks next page button
    Then The table should show the next page of records
    When User clicks previous page button
    Then The table should show the previous page of records

  Scenario: Change rows per page
    When User changes rows per page to "25"
    Then The table should show at most 25 records
