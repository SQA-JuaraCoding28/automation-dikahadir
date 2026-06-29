package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.pages.web.ManagementJabatanPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class ManagementJabatanSteps {

    private final ManagementJabatanPage page = new ManagementJabatanPage();

    // =========================================================================
    // Navigation (sidebar) - mirrors LaporanIzinTerlambatSteps pattern
    // =========================================================================

    @And("web user should see {string} in the sidebar menu")
    public void webUserShouldSeeInTheSidebarMenu(String menuName) {
        if ("Management".equalsIgnoreCase(menuName)) {
            Assert.assertTrue(page.isSidebarManagementDisplayed(), "Sidebar menu 'Management' is not displayed.");
        }
    }

    @And("web user should click on {string} in the sidebar menu")
    public void webUserShouldClickOnInTheSidebarMenu(String menuName) {
        if ("Management".equalsIgnoreCase(menuName)) {
            page.clickSidebarManagement();
        } else if ("Jabatan".equalsIgnoreCase(menuName)) {
            page.clickSidebarJabatan();
        }
    }

    @Then("web user should see the jabatan page")
    public void webUserShouldSeeTheJabatanPage() {
        Assert.assertTrue(page.isJabatanPageDisplayed(), "Jabatan page is not displayed.");
    }

    @Given("user is on the jabatan page")
    public void userIsOnTheJabatanPage() {
        Assert.assertTrue(page.isJabatanPageDisplayed(), "Jabatan page is not displayed.");
    }

    // =========================================================================
    // Search
    // =========================================================================

    @When("user input level {string} in jabatan search box")
    public void userInputLevelInJabatanSearchBox(String level) {
        page.enterSearchLevel(level);
    }

    @Then("jabatan table should be displayed")
    public void jabatanTableShouldBeDisplayed() {
        Assert.assertTrue(page.isJabatanTableDisplayed(), "Jabatan table is not displayed.");
    }

    // =========================================================================
    // Reset
    // =========================================================================

    @When("user clicks the reset button on jabatan page")
    public void userClicksTheResetButtonOnJabatanPage() {
        page.clickReset();
    }

    @Then("jabatan search box should be empty")
    public void jabatanSearchBoxShouldBeEmpty() {
        String value = page.getSearchBoxValue();
        Assert.assertTrue(value == null || value.isEmpty(),
            "Expected search box to be empty after reset, but found: '" + value + "'");
    }

    // =========================================================================
    // Tambah Jabatan (Add)
    // =========================================================================

    @When("user clicks tambahkan button on jabatan page")
    public void userClicksTambahkanButtonOnJabatanPage() {
        page.clickTambahkan();
    }

    @Then("tambah jabatan dialog should be displayed")
    public void tambahJabatanDialogShouldBeDisplayed() {
        Assert.assertTrue(page.isTambahDialogDisplayed(), "Tambah Jabatan dialog is not displayed.");
    }

    @When("user adds jabatan with name {string} and level {string}")
    public void userAddsJabatanWithNameAndLevel(String nama, String level) {
        page.tambahJabatan(nama, level);
    }
}
