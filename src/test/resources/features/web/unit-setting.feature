@management @unit-setting @regression @web
Feature: Unit Setting (Departements Setting)
  As an admin of Hadir
  I want to manage department/unit settings
  So that I can add, toggle selfie requirement, and delete units

  Background:
    Given web user is logged in
    And web user is on the unit setting page

  @smoke @positive
  Scenario: Add a new unit by selecting a department from the dropdown
    When admin clicks the Tambahkan button
    And admin selects nama departemen "IT Support" from the dropdown
    And admin clicks the Tambah submit button
    Then the unit setting action should complete without error

  @positive
  Scenario: Toggle selfie switch on the first unit row
    When admin toggles the selfie switch on the first unit row
    And admin accepts the native browser confirmation
    Then the unit setting action should complete without error

  @positive
  Scenario: Delete the first unit row
    When admin clicks the delete button on the first unit row
    And admin confirms the delete by clicking "Ya"
    Then the unit setting action should complete without error
