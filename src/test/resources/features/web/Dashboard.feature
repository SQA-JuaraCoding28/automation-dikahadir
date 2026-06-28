@web @regression @dashboard
Feature: Dashboard Page Testing

  Scenario Outline: Verify metric cards display and load counts successfully (Positive)
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard
    And User is on the Hadir Dashboard page
    Then The dashboard metric card "<card_title>" should be visible
    And The value of metric card "<card_title>" should load successfully

    Examples:
      | card_title                         |
      | Total semua karyawan               |
      | Total absen hari ini               |
      | Karyawan yang sedang WFH hari ini  |
      | Karyawan yang sedang cuti hari ini |
      | Karyawan yang sedang sakit hari ini |
      | Karyawan yang sedang Izin hari ini |

  Scenario: Profile dropdown menu options (Positive)
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard
    And User is on the Hadir Dashboard page
    When User clicks profile menu dropdown
    Then User should see logout and change password options

  Scenario: Logout successfully (Positive)
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard
    And User is on the Hadir Dashboard page
    When User clicks profile menu dropdown
    And User clicks logout button
    Then User should be redirected to login page

  Scenario: Redirection when accessing dashboard directly unauthorized (Negative)
    Given User attempts to navigate to dashboard page directly without logging in
    Then User should be redirected to login page
