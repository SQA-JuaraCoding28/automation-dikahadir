package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.ImportStatusAktifWebPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

public class ImportStatusAktifWebSteps {

    private WebDriver driver = DriverManager.getDriver();
    private ImportStatusAktifWebPage importPage = new ImportStatusAktifWebPage(driver);

    @When("user navigates to the {string} page")
    public void userNavigatesToThePage(String pageName) {
        if (pageName.equalsIgnoreCase("Import Status Aktif")) {
            importPage.navigateToImportStatusAktif();
        }
    }

    @Then("web user should be on the {string} page")
    public void webUserShouldBeOnThePage(String pageName) {
        if (pageName.equalsIgnoreCase("Import Status Aktif")) {
            importPage.verifyOnPage();
        }
    }

    @And("the {string} and {string} buttons should be displayed")
    public void theButtonsShouldBeDisplayed(String btn1, String btn2) {
        importPage.verifyButtonsDisplayed();
    }

    @And("user clicks the {string} button without selecting a file")
    public void userClicksTheButtonWithoutSelectingAFile(String buttonName) {
        if (buttonName.equalsIgnoreCase("Import")) {
            importPage.clickImportWithoutFile();
        }
    }

    @Then("the system should reject the import action")
    public void theSystemShouldRejectTheImportAction() {
        importPage.verifyActionRejected();
    }
}
