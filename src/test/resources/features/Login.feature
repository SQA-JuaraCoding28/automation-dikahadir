Feature: Hadir Web Admin Login

  Scenario: Successful login with valid credentials (Positive)
    Given User navigates to Hadir login page
    When User logs in with valid admin credentials
    Then User should be successfully logged in

  Scenario: Unsuccessful login with invalid credentials (Negative)
    Given User navigates to Hadir login page
    When User logs in with invalid email "wrongadmin@hadir.com" and password "WrongPassword@123"
    Then User should remain on the login page and see authentication error
