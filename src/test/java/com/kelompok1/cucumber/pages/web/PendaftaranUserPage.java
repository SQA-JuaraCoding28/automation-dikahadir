package com.kelompok1.cucumber.pages.web;

import com.kelompok1.cucumber.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.net.URL;

/**
 * Page Object for Pendaftaran User.
 * URL: https://magang.dikahadir.com/management/user-v2/register
 */
public class PendaftaranUserPage extends BasePage {

    // =====================================================================
    // Sidebar (scoped to the sidebar container so we don't match page titles)
    // =====================================================================
    private final By sidebarManagement = By.xpath(
        "//div[contains(@class,'sidebar')]//p[normalize-space()='Management']");
    private final By sidebarPendaftaranUser = By.xpath(
        "//div[contains(@class,'sidebar')]//p[normalize-space()='Pendaftaran User']");

    // Account Information
    private final By photoUploadInput = By.cssSelector("input[name='logo']");
    private final By nikField         = By.id("nik");
    private final By fullnameField    = By.id("fullname");
    private final By emailField       = By.id("email");
    private final By passwordField    = By.id("password");

    // Work Information (MUI Autocomplete)
    private final By divisiField         = By.id("divisi");
    private final By unitField           = By.id("unit");
    private final By posisiKerjaField    = By.id("posisi-kerja");
    private final By jabatanField        = By.id("jabatan");
    private final By atasanField         = By.id("atasan");
    /** Present but intentionally never interacted with in step defs. */
    private final By atasanClientV2Field = By.id("atasan-v2");
    private final By atasanClientV3Field = By.id("atasan-v3");
    private final By tipeKontrakField    = By.id("tipe-kontrak");

    // Pengaturan Absensi
    private final By lokasiKerjaField  = By.id("lokasi-kerja");
    private final By tipeShiftDropdown = By.id("shift_type");
    private final By jadwalKerjaField  = By.id("jadwal-kerja");
    private final By selfieDropdown    = By.id("required_selfie");
    private final By jadwalKerjaOption = By.xpath("//ul[@role='listbox']/li[contains(normalize-space(.), 'BCA Syariah')]");
    private final By jumlahCutiField   = By.id("jumlah-cuti");

    // Actions
    private final By submitButton = By.cssSelector("button[type='submit']");

    public PendaftaranUserPage() {
        super();
    }

    // =====================================================================
    // Navigation
    // =====================================================================

    public void navigateToPendaftaranUser() {
        // 1. Click Management to expand submenu
        WebElement management = wait.until(ExpectedConditions.elementToBeClickable(sidebarManagement));
        management.click();
        logger.info("Clicked sidebar 'Management'");

        // 2. Wait for the submenu item to be clickable (handles expand animation)
        WebElement pendaftaran = wait.until(ExpectedConditions.elementToBeClickable(sidebarPendaftaranUser));
        pendaftaran.click();
        logger.info("Clicked sidebar 'Pendaftaran User'");

        // 3. Wait for the target URL to load (up to standard.wait seconds)
        boolean urlOk = waitForUrlContains("/management/user-v2/register");
        logger.info("URL contains /management/user-v2/register ? {}", urlOk);

        // 4. Wait for the NIK field to be present (not just visible — it may be scrolled out)
        boolean nikOk = waitForElementPresent(nikField);
        logger.info("NIK field present ? {}", nikOk);
    }

    public boolean isPendaftaranUserPageDisplayed() {
        return getCurrentUrl().contains("/management/user-v2/register")
               && isPresent(nikField);
    }

    // =====================================================================
    // Account Information
    // =====================================================================

    public void uploadPhoto(String path) {
        if (path == null || path.trim().isEmpty()) {
            logger.info("Photo path is empty — skipping upload");
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            URL url = getClass().getClassLoader().getResource(path);
            if (url != null) {
                file = new File(url.getFile());
            }
        }

        if (!file.exists()) {
            throw new RuntimeException("Photo file not found: " + path);
        }

        WebElement hiddenInput = driver.findElement(photoUploadInput);
        hiddenInput.sendKeys(file.getAbsolutePath());
        logger.info("Uploaded photo from: {}", file.getAbsolutePath());
    }

    public void enterNik(String nik) {
        typeText(nikField, nik);
    }

    public void enterNamaKaryawan(String nama) {
        typeText(fullnameField, nama);
    }

    public void enterEmail(String email) {
        typeText(emailField, email);
    }

    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    // =====================================================================
    // Work Information
    // =====================================================================

    public void selectDivisi(String text) {
        selectAutocomplete(divisiField, text);
    }

    public void selectUnit(String text) {
        selectAutocomplete(unitField, text);
    }

    public void selectPosisiKerja(String text) {
        selectAutocomplete(posisiKerjaField, text);
    }

    public void selectJabatan(String text) {
        selectAutocomplete(jabatanField, text);
    }

    public void selectAtasan(String text) {
        selectAutocomplete(atasanField, text);
    }

    public void selectAtasanClientV3(String text) {
        selectAutocomplete(atasanClientV3Field, text);
    }

    public void selectTipeKontrak(String text) {
        selectAutocomplete(tipeKontrakField, text);
    }

    // =====================================================================
    // Pengaturan Absensi
    // =====================================================================

    public void selectLokasiKerja(String text) {
        selectAutocomplete(lokasiKerjaField, text);
    }

    public void selectTipeShift(String optionText) {
        selectMuiSelect(tipeShiftDropdown, optionText);
    }

    public void selectJadwalKerja(String text) {
        selectAutocomplete(jadwalKerjaField, text);
    }

    public void selectSelfie(String optionText) {
        selectMuiSelect(selfieDropdown, optionText);
    }

    public void enterJumlahCuti(String jumlah) {
        typeText(jumlahCutiField, jumlah);
    }

    // =====================================================================
    // Submit & Verification
    // =====================================================================

    public void clickSubmit() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", btn);
        btn.click();
        logger.info("Clicked Submit button");
    }

    public boolean isRedirectedToUserList() {
        return waitForUrlContains("/management/user");
    }

    public boolean isOnPendaftaranUserPage() {
        return getCurrentUrl().contains("/management/user-v2/register");
    }

    // =====================================================================
    // Internal: MUI Autocomplete (type → wait → Arrow Down → Enter)
    // =====================================================================

    private void selectAutocomplete(By inputLocator, String text) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        input.click();
        input.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        input.sendKeys(text);

        By popupOption = By.cssSelector(".MuiAutocomplete-popper li[role='option']");
        wait.until(ExpectedConditions.presenceOfElementLocated(popupOption));

        input.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        logger.debug("Selected autocomplete [{}] with text: {}", inputLocator, text);
    }

    // =====================================================================
    // Internal: MUI Select (dropdown rendered in portal)
    // =====================================================================

    private void selectMuiSelect(By dropdownLocator, String optionText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
        dropdown.click();

        By optionLocator = By.xpath(
            "//ul[@role='listbox']/li[contains(normalize-space(.), '" + optionText + "')]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();

        logger.debug("Selected MUI Select option [{}] from dropdown: {}", optionText, dropdownLocator);
    }
}
