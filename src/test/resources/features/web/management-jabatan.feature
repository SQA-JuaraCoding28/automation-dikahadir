@jabatan @management @web @management
Feature: Menu Jabatan
    As an authorized user
    I want to manage Job Level (Jabatan) data
    So that I can view, filter, and add job levels in the system

    Background:
        Given user is on the web login page
        And user logs in with valid web credentials
        And web user should see "Management" in the sidebar menu
        And web user should click on "Management" in the sidebar menu
        And web user should click on "Jabatan" in the sidebar menu

    @positive @test @smoke
    Scenario: Navigate to jabatan page
        Then web user should be redirected to the dashboard
        And web user should see the jabatan page

    @positive @test
    Scenario: Search jabatan by level
        Given user is on the jabatan page
        When user input level "2" in jabatan search box
        Then jabatan table should be displayed

    @positive @test
    Scenario: Reset jabatan filter
        Given user is on the jabatan page
        When user input level "2" in jabatan search box
        And user clicks the reset button on jabatan page
        Then jabatan search box should be empty

    @positive @test
    Scenario: Add a new jabatan
        Given user is on the jabatan page
        When user clicks tambahkan button on jabatan page
        Then tambah jabatan dialog should be displayed
        When user adds jabatan with name "karyawan" and level "10"
        Then jabatan table should be displayed
