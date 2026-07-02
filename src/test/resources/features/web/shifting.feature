@shifting @web
Feature: Web Shifting Functionality

  @Shifting @positive
  Scenario: Successfully access and view the Shifting data
    Given User berhasil login dan berada di halaman shifting web
    Then the shifting data table should be displayed

  @Shifting @positive
  Scenario: Search for data in Shifting page
    Given User berhasil login dan berada di halaman shifting web
    When user searches for "Shift Pagi" on the shifting page
    Then the shifting data table should be displayed

  @Shifting @negative
  Scenario: Search for non-existent data in Shifting page
    Given User berhasil login dan berada di halaman shifting web
    When user searches for "ShiftMalamMinggu123" on the shifting page
    Then the shifting data table should show empty message
