Feature: Hadir Portal Dashboard Testing

  Scenario Outline: Verify metric cards display and load counts successfully (Positive)
    Given User navigates to Hadir login page
    And User logs in with valid admin credentials
    Then User should be successfully logged in
    And User is on the Hadir Dashboard page
    Then The dashboard metric card "<MetricTitle>" should be visible
    And The value of metric card "<MetricTitle>" should load successfully

    Examples:
      | MetricTitle                         |
      | Total semua karyawan                |
      | Total absen hari ini                |
      | Karyawan yang sedang WFH hari ini   |
      | Karyawan yang sedang cuti hari ini  |
      | Karyawan yang sedang sakit hari ini |
      | Karyawan yang sedang Izin hari ini  |

  Scenario: Verify profile dropdown and elements (Positive)
    Given User navigates to Hadir login page
    And User logs in with valid admin credentials
    Then User should be successfully logged in
    And User is on the Hadir Dashboard page
    When User clicks on the profile menu
    Then Option for changing password and logging out should be displayed

  Scenario: Attempt direct access to dashboard without login (Negative)
    When User attempts to access the dashboard directly without logging in
    Then User should be redirected back to the login page
