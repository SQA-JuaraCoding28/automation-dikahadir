package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.kelompok1.cucumber.pages.BasePage;

/**
 * Page Object for the Import Struktur User page.
 * URL: https://magang.dikahadir.com/imports/import-struktur-users
 *
 * This page allows the admin to:
 * - Upload an Excel file containing user structure data
 * - Download a template Excel file
 */
public class ImportStrukturUserPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By pageHeading = By.xpath(
        "//div[contains(@class, 'MuiAlert') or contains(@class, 'MuiPaper')]"
        + "//p[contains(text(), 'Import Excel Struktur User')]"
        + " | //h1[contains(text(), 'Import')] | //h2[contains(text(), 'Import')]"
        + " | //*[contains(text(), 'Import Excel Struktur User')]");

    private final By fileInput = By.id("selfie");

    private final By importButton = By.xpath(
        "//button[contains(@class, 'MuiButton-containedPrimary') and contains(text(), 'Import')]");

    private final By downloadTemplateButton = By.xpath(
        "//button[contains(@class, 'MuiButton-containedSuccess') and contains(text(), 'Download Template')]");

    // Success / Error alerts (MUI Snackbar or Alert)
    private final By successAlert = By.cssSelector(
        ".MuiAlert-filledSuccess, .MuiAlert-standardSuccess, .MuiAlert-outlinedSuccess");

    private final By errorAlert = By.cssSelector(
        ".MuiAlert-filledError, .MuiAlert-standardError, .MuiAlert-outlinedError");

    private final By snackbarMessage = By.cssSelector(
        ".MuiSnackbar-root, .MuiAlert-message, .notistack-MuiContent, [class*='notistack']");

    private final By anyAlertMessage = By.xpath(
        "//*[contains(@class, 'MuiAlert-message')]"
        + " | //*[contains(@class, 'notistack')]"
        + " | //*[contains(@class, 'Toastify')]"
        + " | //*[contains(@role, 'alert')]");

    // Sidebar nav
    private final By importMenuLink = By.xpath("//p[text()='Import Struktur User']");

    public ImportStrukturUserPage() {
        super();
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateToPage() {
        navigateTo("https://magang.dikahadir.com/imports/import-struktur-users");
        waitForUrlContains("imports/import-struktur-users");
    }

    public boolean isPageDisplayed() {
        try {
            return waitForUrlContains("imports/import-struktur-users");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isHeadingDisplayed() {
        try {
            return isDisplayed(pageHeading);
        } catch (Exception e) {
            return false;
        }
    }

    // ── File Upload ───────────────────────────────────────────────────────────

    /**
     * Upload a file using the hidden file input.
     * Selenium can send keys to file inputs directly, bypassing the OS dialog.
     */
    public void uploadFile(String absoluteFilePath) {
        WebElement input = driver.findElement(fileInput);

        // Make the input visible if it is hidden (common for styled file inputs)
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "arguments[0].style.display='block';"
                + "arguments[0].style.visibility='visible';"
                + "arguments[0].style.opacity='1';",
                input);
        } catch (Exception e) {
            logger.warn("Could not make file input visible via JS: {}", e.getMessage());
        }

        input.sendKeys(absoluteFilePath);
        logger.info("Uploaded file: {}", absoluteFilePath);
    }

    public String getFileInputValidationMessage() {
        WebElement input = driver.findElement(fileInput);
        String message = input.getAttribute("validationMessage");
        logger.info("File input validation message: '{}'", message);
        return message != null ? message : "";
    }

    public String getSelectedFileName() {
        WebElement input = driver.findElement(fileInput);
        String value = input.getAttribute("value");
        if (value != null && value.contains("\\")) {
            value = value.substring(value.lastIndexOf("\\") + 1);
        }
        return value != null ? value : "";
    }

    // ── Buttons ───────────────────────────────────────────────────────────────

    public void clickImport() {
        click(importButton);
        logger.info("Clicked Import button");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void clickDownloadTemplate() {
        click(downloadTemplateButton);
        logger.info("Clicked Download Template button");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public boolean isImportButtonDisplayed() {
        return isDisplayed(importButton);
    }

    public boolean isDownloadTemplateButtonDisplayed() {
        return isDisplayed(downloadTemplateButton);
    }

    public boolean isFileInputDisplayed() {
        return isPresent(fileInput);
    }

    // ── Alerts & Messages ─────────────────────────────────────────────────────

    public boolean isSuccessAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorAlertDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertMessage() {
        try {
            WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(anyAlertMessage));
            String text = el.getText().trim();
            logger.info("Alert message: '{}'", text);
            return text;
        } catch (Exception e) {
            logger.warn("No alert message found");
            return "";
        }
    }

    public boolean isAnyAlertDisplayed() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(anyAlertMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ── Sidebar Navigation ────────────────────────────────────────────────────

    public boolean isImportMenuActive() {
        try {
            WebElement el = driver.findElement(importMenuLink);
            String parentClass = el.findElement(By.xpath("./ancestor::a | ./ancestor::div[contains(@class, 'active')]"))
                .getAttribute("class");
            return parentClass != null && parentClass.contains("active");
        } catch (Exception e) {
            return isPresent(importMenuLink);
        }
    }

    // ── URL Checks ────────────────────────────────────────────────────────────

    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("login");
    }
}
