@absensi @regression @mobile @keluar
Feature: Absensi Keluar (Check-out)
  As a user of Hadir mobile
  I want to check out
  So that my attendance is completed

  Background:
    Given mobile user is logged in and on dashboard

  @positive
  Scenario: Successful absen pulang after checking in
    When user checks if Keluar button is available
    Then skip test if Keluar button is not available
    When user clicks Keluar button on the latest history entry
    And user fills absen pulang form with note "Test absen pulang via automation"
    And user submits absen pulang form
    Then absen pulang should be submitted successfully
    And Absen Masuk button should not be displayed
