package com.kelompok1.cucumber.pages.web;

import com.kelompok1.cucumber.pages.BasePage;
import org.openqa.selenium.By;

public class ManagementJabatanPage extends BasePage {

    // Sidebar navigation locators (same flexible-XPath approach as LaporanIzinTerlambatPage)
    private final By sidebarManagement = By.xpath("//div[contains(@class,'sidebar__item')]//p[normalize-space()='Management']");
    private final By sidebarJabatan = By.xpath("//p[normalize-space()='Jabatan']");

    // Filter / search bar
    private final By searchBox = By.id("search");
    private final By resetButton = By.xpath("//button[contains(@class,'btn-reset')]");
    private final By searchButton = By.xpath("//button[contains(@class,'btn-search')]");

    // Page-level
    private final By tambahkanButton = By.xpath("//button[contains(@class,'button-add')]");
    private final By table = By.cssSelector("table");

    // "Tambah Jabatan" dialog
    private final By dialogTitle = By.id("alert-dialog-title");
    private final By nameField = By.id("name");
    private final By levelField = By.id("level");
    private final By tambahSubmitButton = By.xpath("//form//button[@type='submit' and normalize-space()='Tambah']");

    public ManagementJabatanPage() {
        super();
    }

    // =========================================================================
    // Navigation (sidebar)
    // =========================================================================

    public void clickSidebarManagement() {
        click(sidebarManagement);
    }

    public void clickSidebarJabatan() {
        click(sidebarJabatan);
    }

    public boolean isSidebarManagementDisplayed() {
        return isDisplayed(sidebarManagement);
    }

    public boolean isJabatanPageDisplayed() {
        return getCurrentUrl().contains("/management/job-level") || isDisplayed(table);
    }

    // =========================================================================
    // Filter / Search
    // =========================================================================

    public void enterSearchLevel(String level) {
        typeText(searchBox, level);
        click(searchButton);
    }

    public void clickReset() {
        click(resetButton);
    }

    public String getSearchBoxValue() {
        return findElement(searchBox).getAttribute("value");
    }

    public boolean isJabatanTableDisplayed() {
        return isDisplayed(table);
    }

    // =========================================================================
    // Tambah Jabatan (Add Job Level) dialog
    // =========================================================================

    public void clickTambahkan() {
        click(tambahkanButton);
    }

    public boolean isTambahDialogDisplayed() {
        return isDisplayed(dialogTitle);
    }

    public void enterNamaJabatan(String nama) {
        typeText(nameField, nama);
    }

    public void enterLevelJabatan(String level) {
        typeText(levelField, level);
    }

    public void clickTambahSubmit() {
        click(tambahSubmitButton);
    }

    public void tambahJabatan(String nama, String level) {
        logger.info("Adding Jabatan - nama: {}, level: {}", nama, level);
        enterNamaJabatan(nama);
        enterLevelJabatan(level);
        clickTambahSubmit();
    }
}
