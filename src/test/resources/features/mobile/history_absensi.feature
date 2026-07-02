@history_absensi @mobile
Feature: Mobile History Absensi Functionality

  @HistoryAbsensi @positive
  Scenario: Successfully access and view the History Absensi cards
    Given User berhasil login mobile dan berada di halaman history absensi
    Then the history absensi cards should be displayed
