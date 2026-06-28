@laporan @regression @web
Feature: Laporan Semua Page
  As an admin of Hadir
  I want to view and filter attendance reports on the Laporan Semua page
  So that I can monitor employee attendance records

  Background:
    Given web user is logged in
    And web user is on the laporan semua page

  @smoke @positive
  Scenario: Search attendance report by employee name and date range
    When admin searches laporan by employee name "kelompok"
    And admin selects date range from day 1 to day 30 of June 2026
    And admin clicks the laporan search button
    Then the laporan table should display results

  @positive
  Scenario: Filter attendance report by employee name date range and unit
    When admin searches laporan by employee name "kelompok"
    And admin selects date range from day 1 to day 30 of June 2026
    And admin opens the laporan filter dialog
    And admin selects unit "Sysmex" in the filter
    And admin applies the laporan filter
    And admin clicks the laporan search button
    Then the laporan table should display results

  @positive
  Scenario: Reset button clears the search input
    When admin searches laporan by employee name "kelompok"
    And admin selects date range from day 1 to day 30 of June 2026
    And admin clicks the laporan reset button
    Then the laporan search input should be empty
