@download-absen @regression @web
Feature: Download Absen Report
  As an admin of Hadir
  I want to download attendance reports with various filters
  So that I can analyze employee attendance data

  Background:
    Given web user is logged in
    And web user navigates to Download Absen page via sidebar

  @smoke @positive
  Scenario: Download absen report with all filters applied
    When admin selects absen date range from day 1 month 5 year 2026 to day 30 month 5 year 2026
    And admin selects NIK "D7240029"
    And admin selects nama user "kelompok1_test"
    And admin selects nama upliner "kelompok1_test"
    And admin selects divisi "Accounting"
    And admin selects unit "BCA"
    And admin selects tipe report "Rekap Absen 1"
    And admin clicks the absen download button
    Then download should complete without error

  @positive
  Scenario: Reset button clears all filters on absen page
    When admin selects absen date range from day 1 month 5 year 2026 to day 30 month 5 year 2026
    And admin selects NIK "D7240029"
    And admin clicks the absen reset button
    Then the absen filters should be cleared
