package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.kelompok1.cucumber.pages.BasePage;

/**
 * Page Object for the Unit Setting (Departements Setting) page.
 * URL: https://magang.dikahadir.com/management/departements-settings
 *
 * Three independent flows on this page:
 *   1. Add   -> "Tambahkan" opens a dialog with a MUI Select (not typeable, pick from listbox)
 *   2. Toggle -> selfie switch on a row triggers a NATIVE browser confirm() alert
 *   3. Delete -> trash icon on a row opens a MUI confirm dialog ("Ya" / "Tidak")
 *
 * Per requirements: no table-data assertions after add/toggle/delete — only
 * verifying each action completes without throwing (dialog closes / alert handled).
 * Target row for toggle & delete is always the FIRST row (tr:first-child).
 */
public class UnitSettingPage extends BasePage {

    private static final String PAGE_URL = "https://magang.dikahadir.com/management/departements-settings";

    // =========================================================================
    // ADD DIALOG ("Tambah Departemens Setting")
    // =========================================================================
    // NOTE: exact locator for the "Tambahkan" trigger button is a best guess
    // (text match) — the raw HTML for this specific button was not provided.
    private final By tambahkanButton = By.xpath("//button[contains(.,'Tambahkan')]");

    private final By namaDepartemenSelect = By.xpath(
        "//div[@id='alert-slide-description']//div[@role='combobox']");
    private final By tambahSubmitButton = By.xpath(
        "//div[contains(@class,'MuiDialogActions-root')]//button[@type='submit' and contains(.,'Tambah')]");
    private final By addDialogTitle = By.id("alert-dialog-title");

    // =========================================================================
    // TABLE
    // =========================================================================
    private final By firstRowSelfieCheckbox = By.cssSelector(
        "tbody.MuiTableBody-root tr:first-child input[type='checkbox']");
    private final By firstRowDeleteButton = By.cssSelector(
        "tbody.MuiTableBody-root tr:first-child td:last-child button");
    private final By tableFirstRow = By.cssSelector("tbody.MuiTableBody-root tr:first-child");

    // =========================================================================
    // DELETE CONFIRM DIALOG ("Hapus Departements")
    // =========================================================================
    private final By deleteDialogTitle = By.id("alert-dialog-slide-title");
    private final By deleteYaButton = By.xpath(
        "//div[contains(@class,'MuiDialogActions-root')]//button[@type='submit' and contains(.,'Ya')]");

    private final By managementMenuParent = By.xpath(
        "//p[normalize-space(text())='Management']/ancestor::div[contains(@class,'sidebar__wrapper')]");
    private final By unitSettingMenuItem = By.xpath(
        "//p[normalize-space(text())='Unit Setting']/parent::div/parent::div");

    public UnitSettingPage() {
        super();
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public UnitSettingPage navigateToPage() {
        navigateTo(PAGE_URL);
        return this;
    }

    public boolean isOnPage() {
        return waitForUrlContains("/management/departements-settings");
    }

    public UnitSettingPage navigateViaSidebar() {
        click(managementMenuParent);
        logger.info("Expanded Management sidebar menu");

        // Submenu items are hidden inside a MuiCollapse until expanded —
        // wait for the target item to actually be visible/clickable before
        // clicking it, otherwise the collapse animation isn't finished yet.
        wait.until(ExpectedConditions.elementToBeClickable(unitSettingMenuItem));
        click(unitSettingMenuItem);
        logger.info("Clicked Unit Setting sidebar submenu item");

        isOnPage();
        return this;
    }

    // =========================================================================
    // ADD FLOW
    // =========================================================================

    public void clickTambahkan() {
        click(tambahkanButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addDialogTitle));
        logger.info("Opened Tambah Departemens Setting dialog");
    }

    /**
     * MUI Select (not Autocomplete) — click to open, options render in a
     * listbox popover attached at body level, matched by visible text.
     */
    public void selectNamaDepartemen(String value) {
        click(namaDepartemenSelect);

        By listbox = By.cssSelector("ul[role='listbox']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(listbox));

        By option = By.xpath("//li[@role='option' and contains(.,'" + value + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
        logger.info("Selected nama departemen: '{}'", value);
    }

    public void clickTambahSubmit() {
        click(tambahSubmitButton);
        logger.info("Clicked Tambah submit button");

        // Dialog closes after the brief loading + page refresh — its
        // disappearance is the completion signal for this flow.
        wait.until(ExpectedConditions.invisibilityOfElementLocated(addDialogTitle));
    }

    // =========================================================================
    // TOGGLE SELFIE FLOW
    // =========================================================================

    public void toggleFirstRowSelfieSwitch() {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(firstRowSelfieCheckbox));
        checkbox.click();
        logger.info("Clicked selfie switch on first row");
    }

    public void acceptNativeAlert() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        logger.info("Native alert text: '{}'", alert.getText());
        alert.accept();
        logger.info("Accepted native browser alert");

        // Page reloads/refetches after accept — wait for the table to be
        // present again before the next action runs.
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableFirstRow));
    }

    // =========================================================================
    // DELETE FLOW
    // =========================================================================

    public void clickFirstRowDeleteButton() {
        click(firstRowDeleteButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(deleteDialogTitle));
        logger.info("Opened Hapus Departements confirm dialog");
    }

    public void confirmDelete() {
        click(deleteYaButton);
        logger.info("Clicked 'Ya' to confirm delete");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteDialogTitle));
    }
}
