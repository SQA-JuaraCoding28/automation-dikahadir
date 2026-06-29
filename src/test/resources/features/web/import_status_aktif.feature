@import_status_aktif @regression @web
Feature: Import Status Aktif

  Background:
    Given user is on the web login page
    When user logs in with valid web credentials
    Then web user should be redirected to the dashboard

  @MenuImportStatus @positive
  Scenario: Successfully access Import Status Aktif page and verify elements
    When user navigates to the "Import Status Aktif" page
    Then web user should be on the "Import Status Aktif" page
    And the "Import" and "Download Template" buttons should be displayed

  @MenuImportStatus @negative
  Scenario: Attempt to import without selecting a file
    When user navigates to the "Import Status Aktif" page
    And user clicks the "Import" button without selecting a file
    Then the system should reject the import action
