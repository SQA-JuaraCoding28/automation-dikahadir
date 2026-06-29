@absensi @regression @mobile
Feature: Absensi (Attendance Check-in and Check-out)
  As a user of Hadir mobile
  I want to check in and check out
  So that my attendance is recorded

  Background:
    Given mobile user is logged in and on dashboard

  @smoke @positive
  Scenario: Successful absen masuk with Work From Home
    When user clicks Absen Masuk button on dashboard
    And user takes a photo using camera
    And user fills absen masuk form with Work From Home and note "Test absen via automation"
    And user submits absen masuk form
    Then absen masuk should be submitted successfully
    And history absensi should show new entry for today with type "Work From Home"

  @positive
  Scenario: Successful absen pulang after checking in
    Given mobile user has already checked in today
    When user clicks Keluar button on the latest history entry
    And user fills absen pulang form with note "Test absen pulang via automation"
    And user submits absen pulang form
    Then absen pulang should be submitted successfully
    And history absensi should show updated entry with clock-out time
    And Absen Masuk button should not be displayed
