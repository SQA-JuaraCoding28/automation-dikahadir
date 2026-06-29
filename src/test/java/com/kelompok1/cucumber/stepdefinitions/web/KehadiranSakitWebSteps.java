package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.KehadiranSakitWebPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

public class KehadiranSakitWebSteps {
    private WebDriver driver = DriverManager.getDriver();
    private KehadiranSakitWebPage page = new KehadiranSakitWebPage(driver);

    @When("user clicks on {string} menu")
    public void userClicksOnMenu(String menu) {
        if (menu.equalsIgnoreCase("Sakit")) {
            page.clickMenuSakit();
        } else if (menu.equalsIgnoreCase("Kehadiran")) {
            page.clickMenuKehadiran();
        }
    }

    @Then("web user should be redirected to the {string} page")
    public void webUserShouldBeRedirectedToThePage(String pageType) {
        if (pageType.equalsIgnoreCase("Sakit")) {
            page.verifyOnSakitPage();
        } else if (pageType.equalsIgnoreCase("Kehadiran")) {
            page.verifyOnKehadiranPage();
        }
    }

    @And("the data table should be displayed")
    public void theDataTableShouldBeDisplayed() {
        page.verifyTableDisplayed();
    }

    @And("user searches for {string}")
    public void userSearchesFor(String keyword) {
        page.searchData(keyword);
    }

    @Then("the data table should show empty message")
    public void theDataTableShouldShowEmptyMessage() {
        page.verifyEmptyData();
    }
}
