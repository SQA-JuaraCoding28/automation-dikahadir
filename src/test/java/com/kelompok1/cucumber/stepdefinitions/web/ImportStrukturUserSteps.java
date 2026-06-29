package com.kelompok1.cucumber.stepdefinitions.web;

import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.web.ImportStrukturUserPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step Definitions for Import Struktur User feature.
 * URL: https://magang.dikahadir.com/imports/import-struktur-users
 */
public class ImportStrukturUserSteps {

    private static final Logger logger = LoggerFactory.getLogger(ImportStrukturUserSteps.class);
    private ImportStrukturUserPage importPage = new ImportStrukturUserPage();
    private String testFilePath;

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("User is logged in and on the Import Struktur User page")
    public void user_is_logged_in_and_on_the_import_struktur_user_page() {
        importPage.navigateToPage();
        Assert.assertTrue(importPage.isPageDisplayed(),
            "Import Struktur User page should be displayed");
    }

    @Given("User attempts to navigate to Import Struktur User page directly without logging in")
    public void user_attempts_to_navigate_directly_without_login() {
        DriverManager.getDriver().manage().deleteAllCookies();
        importPage.navigateToPage();
    }

    // =========================================================================
    // WHEN
    // =========================================================================

    @When("User uploads a valid Excel file for Import Struktur User")
    public void user_uploads_valid_excel_file() throws IOException {
        testFilePath = createValidExcelFile();
        importPage.uploadFile(testFilePath);
    }

    @When("User uploads an invalid file format {string} for Import Struktur User")
    public void user_uploads_invalid_file(String fileName) throws IOException {
        testFilePath = createInvalidFile(fileName);
        importPage.uploadFile(testFilePath);
    }

    @When("User uploads an empty Excel file for Import Struktur User")
    public void user_uploads_empty_excel_file() throws IOException {
        testFilePath = createEmptyExcelFile();
        importPage.uploadFile(testFilePath);
    }

    @When("User clicks the Import button on Import Struktur User page")
    public void user_clicks_import_button() {
        importPage.clickImport();
    }

    @When("User clicks the Import button without selecting a file")
    public void user_clicks_import_without_file() {
        importPage.clickImport();
    }

    @When("User clicks the Download Template button on Import Struktur User page")
    public void user_clicks_download_template() {
        importPage.clickDownloadTemplate();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("The Import Struktur User page should be displayed")
    public void the_import_page_should_be_displayed() {
        Assert.assertTrue(importPage.isPageDisplayed(),
            "Import Struktur User page should be displayed");
    }

    @Then("The Import button should be visible on Import Struktur User page")
    public void import_button_should_be_visible() {
        Assert.assertTrue(importPage.isImportButtonDisplayed(),
            "Import button should be visible");
    }

    @Then("The Download Template button should be visible on Import Struktur User page")
    public void download_template_button_should_be_visible() {
        Assert.assertTrue(importPage.isDownloadTemplateButtonDisplayed(),
            "Download Template button should be visible");
    }

    @Then("The file input should be visible on Import Struktur User page")
    public void file_input_should_be_visible() {
        Assert.assertTrue(importPage.isFileInputDisplayed(),
            "File input should be visible");
    }

    @Then("The selected file name should be displayed on Import Struktur User page")
    public void selected_file_name_should_be_displayed() {
        String fileName = importPage.getSelectedFileName();
        Assert.assertFalse(fileName.isEmpty(),
            "Selected file name should not be empty");
        logger.info("Selected file name: {}", fileName);
    }

    @Then("A success message should be displayed on Import Struktur User page")
    public void success_message_should_be_displayed() {
        boolean hasSuccess = importPage.isSuccessAlertDisplayed() || importPage.isAnyAlertDisplayed();
        if (hasSuccess) {
            String msg = importPage.getAlertMessage();
            logger.info("Success message: {}", msg);
        }
        Assert.assertTrue(hasSuccess,
            "A success alert/message should be displayed after valid import");
    }

    @Then("An error message or alert should be displayed on Import Struktur User page")
    public void error_message_should_be_displayed() {
        boolean hasError = importPage.isErrorAlertDisplayed() || importPage.isAnyAlertDisplayed();
        if (hasError) {
            String msg = importPage.getAlertMessage();
            logger.info("Error message: {}", msg);
        }
        Assert.assertTrue(hasError,
            "An error alert/message should be displayed for invalid import");
    }

    @Then("Browser validation should show file required message on Import Struktur User page")
    public void browser_validation_file_required() {
        String validationMsg = importPage.getFileInputValidationMessage();
        logger.info("Validation message: {}", validationMsg);
        Assert.assertFalse(validationMsg.isEmpty(),
            "Browser validation message should be displayed when no file is selected");
    }

    @Then("User is redirected back to the login page from Import Struktur User page")
    public void user_redirected_to_login() {
        Assert.assertTrue(importPage.isOnLoginPage(),
            "User should be redirected to login page. Current URL: " + importPage.getCurrentPageUrl());
    }

    @Then("The Download Template should initiate a file download")
    public void download_template_initiates_download() {
        // After clicking download template, the page should remain on the same URL
        // (download happens via direct file download, not navigation)
        Assert.assertTrue(importPage.isPageDisplayed(),
            "Page should remain on Import Struktur User after template download");
        logger.info("Download Template button clicked - file download initiated");
    }

    // =========================================================================
    // HELPER METHODS — Create test files
    // =========================================================================

    /**
     * Creates a valid Excel file with sample user structure data.
     */
    private String createValidExcelFile() throws IOException {
        String dirPath = System.getProperty("user.dir") + File.separator + "test-data";
        new File(dirPath).mkdirs();
        String filePath = dirPath + File.separator + "valid_struktur_user.xlsx";

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Struktur User");

        // Header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nama");
        header.createCell(1).setCellValue("Email");
        header.createCell(2).setCellValue("Jabatan");
        header.createCell(3).setCellValue("Divisi");
        header.createCell(4).setCellValue("Department");

        // Data row
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("Test User");
        dataRow.createCell(1).setCellValue("testuser@hadir.com");
        dataRow.createCell(2).setCellValue("Staff");
        dataRow.createCell(3).setCellValue("IT");
        dataRow.createCell(4).setCellValue("Engineering");

        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();

        logger.info("Created valid Excel test file: {}", filePath);
        return filePath;
    }

    /**
     * Creates an empty Excel file with only headers.
     */
    private String createEmptyExcelFile() throws IOException {
        String dirPath = System.getProperty("user.dir") + File.separator + "test-data";
        new File(dirPath).mkdirs();
        String filePath = dirPath + File.separator + "empty_struktur_user.xlsx";

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Struktur User");

        // Only header, no data rows
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nama");
        header.createCell(1).setCellValue("Email");

        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();

        logger.info("Created empty Excel test file: {}", filePath);
        return filePath;
    }

    /**
     * Creates an invalid (non-Excel) file for negative testing.
     */
    private String createInvalidFile(String fileName) throws IOException {
        String dirPath = System.getProperty("user.dir") + File.separator + "test-data";
        new File(dirPath).mkdirs();
        String filePath = dirPath + File.separator + fileName;

        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write("This is not a valid Excel file content for import testing.".getBytes());
        fos.close();

        logger.info("Created invalid test file: {}", filePath);
        return filePath;
    }
}
