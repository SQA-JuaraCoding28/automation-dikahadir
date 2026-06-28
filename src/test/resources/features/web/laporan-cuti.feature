@laporan @cuti @regression @web
Feature: Laporan Cuti Page
  As an admin of Hadir
  I want to view and filter leave reports on the Laporan Cuti page
  So that I can monitor employee leave records

  Background:
    Given web user is logged in
    And web user is on the laporan cuti page

  @smoke @positive
  Scenario: Search leave report by employee name and date range
    When admin searches cuti by employee name "sqa"
    And admin selects cuti date range from day 1 month 0 year 2024 to day 1 month 0 year 2026
    And admin clicks the cuti search button
    Then the cuti table should display results

  @positive
  Scenario: Filter leave report by employee name date range and unit
    When admin searches cuti by employee name "sqa"
    And admin selects cuti date range from day 1 month 0 year 2024 to day 1 month 0 year 2026
    And admin opens the cuti filter dialog
    And admin selects cuti unit "Accounting" in the filter
    And admin applies the cuti filter
    And admin clicks the cuti search button
    Then the cuti table should display results

  @positive
  Scenario: Reset button clears the search input on cuti page
    When admin searches cuti by employee name "sqa"
    And admin selects cuti date range from day 1 month 0 year 2024 to day 1 month 0 year 2026
    And admin clicks the cuti reset button
    Then the cuti search input should be empty
