package com.kelompok1.cucumber.stepdefinitions.web;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.web.ImportCutiPage;

/**
 * Step Definitions for Import Cuti page.
 * URL: https://magang.dikahadir.com/imports/import-absen-cuti
 *
 * Background reuses @Given("web user is logged in") from WebLoginSteps.
 */
public class ImportCutiSteps {

    private static final Logger logger = LoggerFactory.getLogger(ImportCutiSteps.class);

    private ImportCutiPage importCutiPage() {
        return new ImportCutiPage();
    }

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("web user is on the import cuti page")
    public void webUserIsOnImportCutiPage() {
        importCutiPage().navigateViaSidebar();
        Assert.assertTrue(importCutiPage().isOnPage(),
            "Should be on import cuti page after navigating via sidebar (Import > Import Cuti)");
        logger.info("Navigated to import cuti page via sidebar");
    }

    // =========================================================================
    // WHEN
    // =========================================================================

    @When("admin chooses a valid excel file for import cuti")
    public void adminChoosesValidExcelFileForImportCuti() {
        importCutiPage().chooseValidExcelFile();
    }

    @When("admin chooses an invalid format file for import cuti")
    public void adminChoosesInvalidFormatFileForImportCuti() {
        importCutiPage().chooseInvalidFormatFile();
    }

    @When("admin clicks the import cuti button")
    public void adminClicksImportCutiButton() {
        importCutiPage().clickImport();
    }

    @When("admin clicks the import cuti button without choosing a file")
    public void adminClicksImportCutiButtonWithoutChoosingFile() {
        importCutiPage().clickImport();
    }

    @When("admin clicks the download template button")
    public void adminClicksDownloadTemplateButton() {
        importCutiPage().clickDownloadTemplate();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("a success notification should be displayed")
    public void aSuccessNotificationShouldBeDisplayed() {
        Assert.assertTrue(importCutiPage().isSuccessNotificationDisplayed(),
            "Expected a success notification after importing a valid file");
        logger.info("Success notification confirmed after import");
    }

    @Then("the template download should trigger without error")
    public void theTemplateDownloadShouldTriggerWithoutError() {
        Assert.assertTrue(importCutiPage().isDownloadTemplateButtonClickable(),
            "Download Template button should be clickable without throwing");
        logger.info("Download Template triggered — file verification is out of scope for this test");
    }

    @Then("the import cuti file format error should be displayed")
    public void theImportCutiFileFormatErrorShouldBeDisplayed() {
        Assert.assertTrue(importCutiPage().isFileFormatErrorDisplayed(),
            "Expected file format error to be displayed for invalid file type");
    }

    @Then("the file format error should contain {string}")
    public void theFileFormatErrorShouldContain(String expectedText) {
        String actualText = importCutiPage().getFileFormatErrorText();
        Assert.assertTrue(actualText.contains(expectedText.replace("*", "")) || actualText.contains(expectedText),
            String.format("Expected file format error to contain '%s' but got: '%s'", expectedText, actualText));
        logger.info("File format error text confirmed: '{}'", actualText);
    }

    @Then("a native browser alert should be displayed for import cuti")
    public void aNativeBrowserAlertShouldBeDisplayedForImportCuti() {
        Assert.assertTrue(importCutiPage().isFileInputValidationMessageDisplayed(),
            "Expected native browser validation message when attempting import without selecting a file");
    }
}
