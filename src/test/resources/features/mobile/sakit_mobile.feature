@sakit_mobile @mobile
Feature: Mobile Sakit Functionality

  @SakitMobile @positive
  Scenario: Successfully access and view the List Request Sakit
    Given User berhasil login mobile dan berada di halaman sakit mobile
    Then the sakit request cards should be displayed
