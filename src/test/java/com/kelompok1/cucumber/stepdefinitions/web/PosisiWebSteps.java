package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.PosisiWebPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PosisiWebSteps {

    private PosisiWebPage posisiPage() {
        return new PosisiWebPage(DriverManager.getDriver());
    }

    @Given("User berhasil login dan berada di halaman posisi web")
    public void userBerhasilLoginDanBeradaDiHalamanPosisiWeb() {
        com.kelompok1.cucumber.pages.web.LoginWebPage loginPage = new com.kelompok1.cucumber.pages.web.LoginWebPage();
        loginPage.navigateToLoginPage();
        try { Thread.sleep(2000); } catch(Exception e){}
        loginPage.doLogin("admin@hadir.com", "MagangSQA_JC@123");
        try { Thread.sleep(3000); } catch(Exception e){}
        posisiPage().navigateToPosisi();
    }

    @When("user searches for {string} on the posisi page")
    public void userSearchesForOnThePosisiPage(String query) {
        posisiPage().searchData(query);
    }

    @Then("the posisi data table should be displayed")
    public void thePosisiDataTableShouldBeDisplayed() {
        posisiPage().verifyTableDisplayed();
    }

    @Then("the posisi data table should show empty message")
    public void thePosisiDataTableShouldShowEmptyMessage() {
        posisiPage().verifyEmptyTable();
    }
}
