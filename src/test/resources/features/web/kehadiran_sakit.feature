@kehadiran_sakit @regression @web
Feature: Menu Kehadiran dan Sakit
  As an admin of Hadir
  I want to view sick and attendance data
  So that I can monitor employee data

  Background:
    Given user is on the web login page
    When user logs in with valid web credentials
    Then web user should be redirected to the dashboard

  @MenuSakit @positive
  Scenario: Successfully access and view the Sick (Sakit) data
    When user clicks on "Sakit" menu
    Then web user should be redirected to the "Sakit" page
    And the data table should be displayed

  @MenuKehadiran @positive
  Scenario: Successfully access and view the Attendance (Kehadiran) data
    When user clicks on "Kehadiran" menu
    Then web user should be redirected to the "Kehadiran" page
    And the data table should be displayed

  @MenuSakit @negative
  Scenario: Search for non-existent data in Sick (Sakit) page
    When user clicks on "Sakit" menu
    And user searches for "DataKaryawanTidakAda123"
    Then the data table should show empty message
