@web @regression @import-struktur-user
Feature: Import Struktur User Page Testing
  As an admin of Hadir
  I want to import user structure data via Excel file
  So that I can manage user structures efficiently

  Background:
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard

  # ── Positive Scenarios ─────────────────────────────────────────────────────

  @positive @smoke
  Scenario: Verify Import Struktur User page is accessible
    And User is logged in and on the Import Struktur User page
    Then The Import Struktur User page should be displayed
    And The Import button should be visible on Import Struktur User page
    And The Download Template button should be visible on Import Struktur User page
    And The file input should be visible on Import Struktur User page

  @positive
  Scenario: Upload valid Excel file for Import Struktur User
    And User is logged in and on the Import Struktur User page
    When User uploads a valid Excel file for Import Struktur User
    Then The selected file name should be displayed on Import Struktur User page

  @positive
  Scenario: Import valid Excel file successfully
    And User is logged in and on the Import Struktur User page
    When User uploads a valid Excel file for Import Struktur User
    And User clicks the Import button on Import Struktur User page
    Then A success message should be displayed on Import Struktur User page

  @positive
  Scenario: Download Template file
    And User is logged in and on the Import Struktur User page
    When User clicks the Download Template button on Import Struktur User page
    Then The Download Template should initiate a file download

  # ── Negative Scenarios ─────────────────────────────────────────────────────

  @negative
  Scenario: Import without selecting a file
    And User is logged in and on the Import Struktur User page
    When User clicks the Import button without selecting a file
    Then Browser validation should show file required message on Import Struktur User page

  @negative
  Scenario: Upload invalid file format (TXT)
    And User is logged in and on the Import Struktur User page
    When User uploads an invalid file format "invalid_file.txt" for Import Struktur User
    And User clicks the Import button on Import Struktur User page
    Then An error message or alert should be displayed on Import Struktur User page

  @negative
  Scenario: Upload invalid file format (PDF)
    And User is logged in and on the Import Struktur User page
    When User uploads an invalid file format "invalid_file.pdf" for Import Struktur User
    And User clicks the Import button on Import Struktur User page
    Then An error message or alert should be displayed on Import Struktur User page

  @negative
  Scenario: Upload empty Excel file
    And User is logged in and on the Import Struktur User page
    When User uploads an empty Excel file for Import Struktur User
    And User clicks the Import button on Import Struktur User page
    Then An error message or alert should be displayed on Import Struktur User page

  @negative
  Scenario: Direct access to Import Struktur User page without login
    Given User attempts to navigate to Import Struktur User page directly without logging in
    Then User is redirected back to the login page from Import Struktur User page
