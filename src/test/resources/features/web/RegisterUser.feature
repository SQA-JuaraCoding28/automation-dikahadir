@web @regression @register-user
Feature: Registrasi User Page Testing

  Background:
    Given user is on the web login page
    And user logs in with valid web credentials
    Then web user should be redirected to the dashboard
    And User is on the Register User page

  # =====================================================================
  # POSITIVE SCENARIOS
  # =====================================================================

  @positive
  Scenario: Verify Register User page elements are displayed (Positive)
    Then Register page should display the heading "Registrasi User"
    And Submit button should be present on register form

  @positive
  Scenario: Fill Account Information fields (Positive)
    When User enters NIK "EMP-TEST-001"
    And User enters Nama Karyawan "Test Automation User"
    And User enters registration email "testautomation@example.com"
    And User enters registration password "Password@123"
    Then User should remain on the Register User page

  @positive
  Scenario: Select Work Information dropdowns using first available options (Positive)
    When User selects the first available Divisi
    And User selects the first available Unit
    And User selects the first available Posisi Kerja
    And User selects the first available Jabatan
    And User selects the first available Tipe Kontrak
    Then User should remain on the Register User page

  @positive
  Scenario: Select Attendance Settings with first available option (Positive)
    When User selects the first available Jadwal Kerja
    Then User should remain on the Register User page

  # =====================================================================
  # NEGATIVE SCENARIOS
  # =====================================================================

  @negative
  Scenario: Submit form with empty fields (Negative)
    When User clicks submit button on register form
    Then The form should have validation errors

  @negative
  Scenario: Submit form with missing required Work Information (Negative)
    When User enters NIK "EMP-NEG-001"
    And User enters Nama Karyawan "Negative Test User"
    And User enters registration email "negtest@example.com"
    And User enters registration password "Password@123"
    And User clicks submit button on register form
    Then The form should have validation errors

  @negative
  Scenario: Submit form with invalid email format (Negative)
    When User enters NIK "EMP-NEG-002"
    And User enters Nama Karyawan "Invalid Email User"
    And User enters registration email "invalid-email-format"
    And User enters registration password "Password@123"
    And User clicks submit button on register form
    Then The form should have validation errors

  @negative
  Scenario: Direct access to Register User page without login (Negative)
    Given User attempts to navigate to Register User page directly without logging in
    Then User is redirected to the login page from register
