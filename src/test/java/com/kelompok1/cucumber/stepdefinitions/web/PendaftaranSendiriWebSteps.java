package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.PendaftaranSendiriWebPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PendaftaranSendiriWebSteps {

    private PendaftaranSendiriWebPage pendaftaranPage() {
        return new PendaftaranSendiriWebPage(DriverManager.getDriver());
    }

    @Given("User berhasil login dan berada di halaman pendaftaran sendiri web")
    public void userBerhasilLoginDanBeradaDiHalamanPendaftaranSendiriWeb() {
        com.kelompok1.cucumber.pages.web.LoginWebPage loginPage = new com.kelompok1.cucumber.pages.web.LoginWebPage();
        loginPage.navigateToLoginPage();
        try { Thread.sleep(2000); } catch(Exception e){}
        loginPage.doLogin("admin@hadir.com", "MagangSQA_JC@123");
        try { Thread.sleep(3000); } catch(Exception e){}
        pendaftaranPage().navigateToPendaftaran();
    }

    @Then("the {string} table should be displayed")
    public void theTableShouldBeDisplayed(String tableTitle) {
        pendaftaranPage().verifyPageTitle(tableTitle);
        pendaftaranPage().verifyTableDisplayed();
    }

    @When("user searches for {string} on the pendaftaran page")
    public void userSearchesForOnThePendaftaranPage(String query) {
        pendaftaranPage().searchData(query);
    }

    @Then("the pendaftaran data table should be displayed")
    public void theDataTableShouldBeDisplayed() {
        pendaftaranPage().verifyTableDisplayed();
    }

    @Then("the pendaftaran data table should show empty message")
    public void theDataTableShouldShowEmptyMessage() {
        pendaftaranPage().verifyEmptyTable();
    }
}
