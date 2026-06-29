package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.kelompok1.cucumber.pages.BasePage;

import java.util.List;

/**
 * Page Object for the User Registration page.
 * URL: https://magang.dikahadir.com/management/user-v2/register
 *
 * Contains Account Information, Work Information, and Attendance settings.
 * Uses JavascriptExecutor for MUI Autocomplete interaction because React's
 * synthetic event system requires nativeInputValueSetter for proper state updates.
 */
public class RegisterUserPage extends BasePage {

    // === Account Information ===
    private final By nikInput = By.id("nik");
    private final By fullnameInput = By.id("fullname");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");

    // === Work Information (Autocomplete/Combobox) ===
    private final By divisiInput = By.id("divisi");
    private final By unitInput = By.id("unit");
    private final By posisiKerjaInput = By.id("posisi-kerja");
    private final By jabatanInput = By.id("jabatan");
    private final By tipeKontrakInput = By.id("tipe-kontrak");

    // === Attendance Settings ===
    private final By lokasiKerjaInput = By.id("lokasi-kerja");
    private final By jadwalKerjaInput = By.id("jadwal-kerja");
    private final By jumlahCutiInput = By.id("jumlah-cuti");

    // === Buttons ===
    private final By submitButton = By.xpath("//button[text()='Submit' or contains(text(), 'Submit')]");

    // === Validation / Toast / Alert ===
    private final By successAlert = By.xpath("//div[contains(@class, 'MuiAlert-filledSuccess')] | //div[contains(@class, 'Toastify')]//div[contains(@class, 'toast--success')] | //div[contains(@class, 'notistack-MuiContent-success')]");
    private final By errorAlert = By.xpath("//div[contains(@class, 'MuiAlert-filledError')] | //div[contains(@class, 'Toastify')]//div[contains(@class, 'toast--error')] | //div[contains(@class, 'notistack-MuiContent-error')] | //div[contains(@class, 'MuiAlert-standardError')]");
    private final By helperTexts = By.xpath("//p[contains(@class, 'MuiFormHelperText-root') and contains(@class, 'Mui-error')]");
    private final By pageHeading = By.xpath("//h1[contains(text(), 'Registrasi User')]");

    public RegisterUserPage() {
        super();
    }

    public void navigateToPage() {
        navigateTo("https://magang.dikahadir.com/management/user-v2/register");
        waitForUrlContains("management/user-v2/register");
    }

    public boolean isPageDisplayed() {
        return isDisplayed(pageHeading);
    }

    // === Account Information Methods ===

    public void enterNik(String nik) {
        typeText(nikInput, nik);
    }

    public void enterFullname(String name) {
        typeText(fullnameInput, name);
    }

    public void enterEmail(String email) {
        typeText(emailInput, email);
    }

    public void enterPassword(String password) {
        typeText(passwordInput, password);
    }

    // === Autocomplete Dropdown Methods ===

    /**
     * Selects a value from an MUI Autocomplete dropdown using JavaScript
     * to properly trigger React's synthetic event system.
     *
     * Strategy:
     * 1. Use JS to set input value and dispatch React-compatible input event
     * 2. Wait for API results to load
     * 3. Click the first matching option or the first available option
     */
    private void selectAutocompleteOption(String fieldId, String searchValue) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Step 1: Focus the input and trigger React-compatible value change
        String script = 
            "var input = document.getElementById(arguments[0]);" +
            "input.focus();" +
            "input.click();" +
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
            "  window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(input, arguments[1]);" +
            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
            "return true;";
        
        js.executeScript(script, fieldId, searchValue);
        logger.info("Triggered autocomplete search for '{}' in field '{}'", searchValue, fieldId);
        
        // Step 2: Wait for API results to load
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        
        // Step 3: Try to click the first matching option
        try {
            By exactOption = By.xpath("//li[@role='option' and contains(text(), '" + searchValue + "')]");
            List<WebElement> options = findAll(exactOption);
            if (!options.isEmpty()) {
                options.get(0).click();
                logger.info("Selected option '{}' in field '{}'", searchValue, fieldId);
            } else {
                // Fallback: click any first available option
                By anyOption = By.xpath("//li[@role='option']");
                List<WebElement> anyOptions = findAll(anyOption);
                if (!anyOptions.isEmpty()) {
                    String text = anyOptions.get(0).getText();
                    anyOptions.get(0).click();
                    logger.info("Selected first available option '{}' in field '{}'", text, fieldId);
                } else {
                    logger.warn("No options available in autocomplete for field '{}'", fieldId);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to select autocomplete option for field '{}': {}", fieldId, e.getMessage());
        }
        
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    /**
     * Selects the first available option from an autocomplete dropdown.
     * Used when we don't care about the specific value, just need something selected.
     */
    private void selectFirstAutocompleteOption(String fieldId) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Focus and open the dropdown with empty search to load all options
        String script = 
            "var input = document.getElementById(arguments[0]);" +
            "input.focus();" +
            "input.click();" +
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(" +
            "  window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(input, '');" +
            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
            "return true;";
        
        js.executeScript(script, fieldId);
        
        // Wait for API results
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
        
        // Click the first available option
        try {
            By anyOption = By.xpath("//li[@role='option']");
            List<WebElement> options = findAll(anyOption);
            if (!options.isEmpty()) {
                String text = options.get(0).getText();
                options.get(0).click();
                logger.info("Selected first option '{}' in field '{}'", text, fieldId);
            } else {
                logger.warn("No options available for field '{}'", fieldId);
            }
        } catch (Exception e) {
            logger.error("Failed to select first option for field '{}': {}", fieldId, e.getMessage());
        }
        
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void selectDivisi(String value) {
        selectAutocompleteOption("divisi", value);
    }

    public void selectUnit(String value) {
        selectAutocompleteOption("unit", value);
    }

    public void selectPosisiKerja(String value) {
        selectAutocompleteOption("posisi-kerja", value);
    }

    public void selectJabatan(String value) {
        selectAutocompleteOption("jabatan", value);
    }

    public void selectTipeKontrak(String value) {
        selectAutocompleteOption("tipe-kontrak", value);
    }

    public void selectLokasiKerja(String value) {
        selectAutocompleteOption("lokasi-kerja", value);
    }

    public void selectJadwalKerja(String value) {
        selectAutocompleteOption("jadwal-kerja", value);
    }

    // === "First available" selection methods for cascading dropdowns ===
    
    public void selectFirstDivisi() {
        selectFirstAutocompleteOption("divisi");
    }

    public void selectFirstUnit() {
        selectFirstAutocompleteOption("unit");
    }

    public void selectFirstPosisiKerja() {
        selectFirstAutocompleteOption("posisi-kerja");
    }

    public void selectFirstJabatan() {
        selectFirstAutocompleteOption("jabatan");
    }

    public void selectFirstTipeKontrak() {
        selectFirstAutocompleteOption("tipe-kontrak");
    }

    public void selectFirstJadwalKerja() {
        selectFirstAutocompleteOption("jadwal-kerja");
    }

    public void enterJumlahCuti(String amount) {
        WebElement input = findElement(jumlahCutiInput);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(amount);
    }

    // === Form Actions ===

    public void clickSubmit() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(submitButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
        btn.click();
        logger.info("Clicked Submit button on register form");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    // === Validation & Result Methods ===

    public boolean isSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasValidationErrors() {
        try {
            return !findAll(helperTexts).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return getText(errorAlert);
    }

    public boolean isFieldRequired(String fieldId) {
        try {
            WebElement field = driver.findElement(By.id(fieldId));
            return field.getAttribute("required") != null || 
                   "true".equals(field.getAttribute("aria-required"));
        } catch (Exception e) {
            return false;
        }
    }

    public String getNikValue() {
        return findElement(nikInput).getAttribute("value");
    }

    public String getFullnameValue() {
        return findElement(fullnameInput).getAttribute("value");
    }

    public String getEmailValue() {
        return findElement(emailInput).getAttribute("value");
    }

    public boolean isSubmitButtonPresent() {
        return isDisplayed(submitButton);
    }

    public String getValidationMessageForField(String fieldId) {
        try {
            WebElement field = driver.findElement(By.id(fieldId));
            return (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].validationMessage;", field);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isStillOnRegisterPage() {
        return getCurrentUrl().contains("management/user-v2/register");
    }
}
