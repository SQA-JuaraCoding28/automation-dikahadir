package com.kelompok1.cucumber.stepdefinitions.mobile;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.mobile.HistoryAbsensiMobilePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class HistoryAbsensiMobileSteps {

    private HistoryAbsensiMobilePage historyPage() {
        return new HistoryAbsensiMobilePage(DriverManager.getDriver());
    }

    @Given("User berhasil login mobile dan berada di halaman history absensi")
    public void userBerhasilLoginMobileDanBeradaDiHalamanHistoryAbsensi() {
        com.kelompok1.cucumber.pages.mobile.LoginMobilePage loginPage = new com.kelompok1.cucumber.pages.mobile.LoginMobilePage();
        loginPage.navigateToLoginPage();
        try { Thread.sleep(2000); } catch(Exception e){}
        loginPage.doLogin("admin@hadir.com", "MagangSQA_JC@123");
        try { Thread.sleep(3000); } catch(Exception e){}
        historyPage().navigateToHistoryAbsensi();
    }

    @Then("the history absensi cards should be displayed")
    public void theHistoryAbsensiCardsShouldBeDisplayed() {
        historyPage().verifyCardsDisplayed();
    }
}
