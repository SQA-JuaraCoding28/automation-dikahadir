@login @regression
Feature: Login to Hadir Application
  As a user of Hadir
  I want to be able to login with my credentials
  So that I can access the attendance system

  Background:
    Given user is on the Hadir login page

  @smoke @positive @happy-path
  Scenario: Successful login with valid credentials
    When user logs in with valid credentials
    Then user should be redirected to the dashboard

  @negative
  Scenario: Login with empty email and password
    When user logs in with email "" and password ""
    Then error message should be displayed
    And error message should contain "Akun tidak ditemukan"
    And user should remain on the login page

  @negative
  Scenario: Login with valid email but wrong password
    When user logs in with email "hadirsqa1@gmail.com" and password "wrongpassword"
    Then error message should be displayed
    And error message should contain "Email atau password salah"
    And user should remain on the login page

  @negative
  Scenario: Login with email missing @ symbol
    When user enters email "hadirsqa1gmail.com" and password "password" without submitting
    Then browser validation should contain "Please include an '@' in the email address"
    And user should remain on the login page
