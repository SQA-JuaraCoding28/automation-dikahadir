@import @import-cuti @regression @web
Feature: Import Cuti
  As an admin of Hadir
  I want to import leave (cuti) data via Excel file
  So that I can bulk-upload leave records instead of entering them manually

  Background:
    Given web user is logged in
    And web user is on the import cuti page

  @smoke @positive
  Scenario: Successfully import a valid Excel file
    When admin chooses a valid excel file for import cuti
    And admin clicks the import cuti button
    Then a success notification should be displayed

  @positive
  Scenario: Download the import cuti template
    When admin clicks the download template button
    Then the template download should trigger without error

  @negative
  Scenario: Attempt to import a file with invalid format
    When admin chooses an invalid format file for import cuti
    And admin clicks the import cuti button
    Then the import cuti file format error should be displayed
    And the file format error should contain "*File harus berupa file Excel (.xls atau .xlsx)"

  @negative
  Scenario: Attempt to import without choosing a file
    When admin clicks the import cuti button without choosing a file
    Then a native browser alert should be displayed for import cuti
