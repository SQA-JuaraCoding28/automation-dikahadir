package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.kelompok1.cucumber.pages.BasePage;

import java.nio.file.Paths;

/**
 * Page Object for the Import Cuti page.
 * URL: https://magang.dikahadir.com/imports/import-absen-cuti
 *
 * File input is a native HTML5 <input type="file">, so sendKeys() with an
 * absolute file path works directly — no OS file dialog is involved,
 * unlike a real user's browser interaction.
 */
public class ImportCutiPage extends BasePage {

    private static final String PAGE_URL = "https://magang.dikahadir.com/imports/import-absen-cuti";

    // Test fixture files — must exist on disk at these exact paths.
    private static final String VALID_EXCEL_FILE    = "src/test/resources/test-data-excel/Template_Cuti_Hadir.xlsx";
    private static final String INVALID_FORMAT_FILE = "src/test/resources/test-data-image/image.jpeg";
    // =========================================================================
    // SIDEBAR NAVIGATION
    // =========================================================================
    private final By sidebarWrapper = By.cssSelector(".sidebar__wrapper");
    private final By importMenuParent = By.xpath(
        "//p[normalize-space(text())='Import']/ancestor::div[contains(@class,'sidebar__wrapper')]");
    private final By importCutiMenuItem = By.xpath(
        "//p[normalize-space(text())='Import Cuti']/parent::div/parent::div");

    // =========================================================================
    // FORM ELEMENTS
    // =========================================================================
    private final By fileInput             = By.id("file_excel");
    private final By importButton          = By.xpath("//button[@type='submit' and contains(.,'Import')]");
    private final By downloadTemplateButton = By.xpath("//button[@type='button' and contains(.,'Download Template')]");

    // NOTE: pending confirmation from screenshot — best-guess MUI snackbar/alert locator.
    private final By successNotification = By.xpath(
        "//*[contains(@class,'MuiSnackbar-root')][.//text()[contains(.,'Berhasil import excel')]]");

    private final By fileFormatError = By.id("file_excel-helper-text");

    public ImportCutiPage() {
        super();
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public ImportCutiPage navigateViaSidebar() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarWrapper));

        click(importMenuParent);
        logger.info("Expanded Import sidebar menu");

        wait.until(ExpectedConditions.elementToBeClickable(importCutiMenuItem));
        click(importCutiMenuItem);
        logger.info("Clicked Import Cuti sidebar submenu item");

        isOnPage();
        return this;
    }

    public boolean isOnPage() {
        return waitForUrlContains("/imports/import-absen-cuti");
    }

    // =========================================================================
    // FILE SELECTION
    // =========================================================================

    public void chooseValidExcelFile() {
        String absolutePath = Paths.get(VALID_EXCEL_FILE).toAbsolutePath().toString();
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        input.sendKeys(absolutePath);
        logger.info("Selected valid excel file: {}", absolutePath);
    }

    public void chooseInvalidFormatFile() {
        String absolutePath = Paths.get(INVALID_FORMAT_FILE).toAbsolutePath().toString();
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        input.sendKeys(absolutePath);
        logger.info("Selected invalid format file: {}", absolutePath);
    }

    // =========================================================================
    // ACTIONS
    // =========================================================================

    public void clickImport() {
        click(importButton);
        logger.info("Clicked Import button");
    }

    public void clickDownloadTemplate() {
        click(downloadTemplateButton);
        logger.info("Clicked Download Template button");
    }

    // =========================================================================
    // ASSERTIONS / STATE CHECKS
    // =========================================================================

    public boolean isSuccessNotificationDisplayed() {
        return isDisplayed(successNotification);
    }

    public String getSuccessNotificationText() {
        return getText(By.xpath("//div[contains(@class,'MuiSnackbar-root')]"));
    }

    public boolean isDownloadTemplateButtonClickable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(downloadTemplateButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFileFormatErrorDisplayed() {
        return isDisplayed(fileFormatError);
    }

    public String getFileFormatErrorText() {
        return getText(fileFormatError);
    }

    public void dismissNativeAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            logger.info("Native alert text: '{}'", alert.getText());
            alert.accept();
        } catch (Exception e) {
            logger.warn("No native alert present to dismiss");
        }
    }

    public String getFileInputValidationMessage() {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        String message = input.getAttribute("validationMessage");
        logger.info("File input browser validation message: '{}'", message);
        return message;
    }

    public boolean isFileInputValidationMessageDisplayed() {
        return !getFileInputValidationMessage().isEmpty();
    }
}
