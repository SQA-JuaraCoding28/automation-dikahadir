@pendaftaran_sendiri @web
Feature: Web Pendaftaran Sendiri Functionality

  @Pendaftaran @positive
  Scenario: Successfully access and view the Pendaftaran Sendiri data
    Given User berhasil login dan berada di halaman pendaftaran sendiri web
    Then the "Data User Registrasi Mobile" table should be displayed

  @Pendaftaran @positive
  Scenario: Search for data in Pendaftaran Sendiri page
    Given User berhasil login dan berada di halaman pendaftaran sendiri web
    When user searches for "Reza" on the pendaftaran page
    Then the pendaftaran data table should be displayed

  @Pendaftaran @negative
  Scenario: Search for non-existent data in Pendaftaran Sendiri page
    Given User berhasil login dan berada di halaman pendaftaran sendiri web
    When user searches for "DataTidakAda123" on the pendaftaran page
    Then the pendaftaran data table should show empty message
