@posisi @web
Feature: Web Posisi Functionality

  @Posisi @positive
  Scenario: Successfully access and view the Posisi data
    Given User berhasil login dan berada di halaman posisi web
    Then the posisi data table should be displayed

  @Posisi @positive
  Scenario: Search for data in Posisi page
    Given User berhasil login dan berada di halaman posisi web
    When user searches for "1Juara" on the posisi page
    Then the posisi data table should be displayed

  @Posisi @negative
  Scenario: Search for non-existent data in Posisi page
    Given User berhasil login dan berada di halaman posisi web
    When user searches for "DataPosisiTidakAda123" on the posisi page
    Then the posisi data table should show empty message
