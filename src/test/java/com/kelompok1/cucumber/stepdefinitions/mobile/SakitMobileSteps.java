package com.kelompok1.cucumber.stepdefinitions.mobile;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.mobile.SakitMobilePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SakitMobileSteps {

    private SakitMobilePage sakitPage() {
        return new SakitMobilePage(DriverManager.getDriver());
    }

    @Given("User berhasil login mobile dan berada di halaman sakit mobile")
    public void userBerhasilLoginMobileDanBeradaDiHalamanSakitMobile() {
        com.kelompok1.cucumber.pages.mobile.LoginMobilePage loginPage = new com.kelompok1.cucumber.pages.mobile.LoginMobilePage();
        loginPage.navigateToLoginPage();
        try { Thread.sleep(2000); } catch(Exception e){}
        loginPage.doLogin("admin@hadir.com", "MagangSQA_JC@123");
        try { Thread.sleep(3000); } catch(Exception e){}
        sakitPage().navigateToSakit();
    }

    @Then("the sakit request cards should be displayed")
    public void theSakitRequestCardsShouldBeDisplayed() {
        sakitPage().verifyCardsDisplayed();
    }
}
