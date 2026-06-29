package com.kelompok1.cucumber.pages.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.kelompok1.cucumber.core.ConfigReader;
import com.kelompok1.cucumber.core.DriverManager;
import com.kelompok1.cucumber.pages.BasePage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Page Object for Absensi (Check-in / Check-out) flow.
 * Shared by both AbsensiMasukSteps and AbsensiKeluarSteps.
 */
public class AbsensiPage extends BasePage {

    // =========================================================================
    // CAMERA SCREEN
    // =========================================================================
    private final By cameraVideo = By.cssSelector("video.camera__capture");
    private final By cameraButton = By.cssSelector("svg.feather-camera");
    private final By closeCameraButton = By.cssSelector("svg.feather-x");


    // =========================================================================
    // BROWSER POPUP (Change password, etc.)
    // =========================================================================
    private final By passwordPopup = By.xpath("//div[contains(text(),'Change your password')]");
    private final By passwordPopupOk = By.xpath("//button[contains(.,'OK')]");

    // =========================================================================
    // ABSEN MASUK FORM
    // =========================================================================
    private final By jamAbsenField = By.xpath(
        "//label[contains(text(),'Jam Absen')]/following::input[@type='text'][1]");
    private final By tipeAbsenDropdown = By.id("mui-component-select-is_wfh");
    private final By tipeAbsenOptionWfh = By.xpath("//li[@data-value='true']");
    private final By tipeAbsenOptionWfo = By.xpath("//li[@data-value='false']");
    private final By tipeAbsenListbox = By.cssSelector("ul[role='listbox']");
    private final By noteFieldMasuk = By.xpath(
        "//label[contains(text(),'Note')]/following::input[@name='notes'][1]");
    private final By absenMasukSubmitButton = By.xpath(
        "//button[@type='submit' and contains(.,'Absen Masuk')]");

    // =========================================================================
    // ABSEN PULANG FORM (Keluar flow)
    // =========================================================================
    private final By timeOutField = By.cssSelector("input[name='time_out']");
    private final By noteFieldPulang = By.cssSelector("textarea[name='notes']");
    private final By absenPulangSubmitButton = By.xpath(
        "//button[@type='submit' and contains(.,'Absen Pulang')]");

    // =========================================================================
    // HISTORY ABSENSI
    // =========================================================================
    private final By historySection = By.xpath("//p[contains(text(),'History Absensi')]");
    private final By historyEntries = By.xpath(
        "//div[contains(@class,'css-10w23i8')]");
    private final By historyEntryName = By.xpath(
        ".//p[contains(@class,'css-1t2dqf9')]");
    private final By historyEntryDate = By.xpath(
        ".//p[contains(@class,'css-td73y7')]");
    private final By historyEntryType = By.xpath(
        ".//p[contains(@class,'css-a6q5qi')]");
    private final By historyEntryTime = By.xpath(
        ".//p[contains(@class,'css-8otdjv')]");

    // =========================================================================
    // KELUAR BUTTON
    //
    // Previous locator: //button[contains(.,'Keluar')]
    // Problem: MUI renders the label inside a nested <p> tag, so the text node
    // is not a direct child of <button>. Some browser/driver combos also
    // mis-resolve the string-value of a button with mixed SVG + text children,
    // causing contains(., 'Keluar') to fail even though the text is visible.
    //
    // Fix: target data-testid="LogoutIcon" on the SVG inside the button.
    // This attribute is injected by MUI's icon system and is stable across
    // CSS class renames. The ancestor::button[1] walk is unambiguous.
    // =========================================================================
    private final By keluarButton = By.xpath(
        "//button[.//*[@data-testid='LogoutIcon']]");

    // =========================================================================
    // PERMISSION ERROR DIALOG
    // =========================================================================
    private final By permissionDialog = By.xpath(
        "//div[@role='dialog']//p[contains(text(),'Lokasi harus diizinkan')]");
    private final By permissionDialogOkButton = By.xpath(
        "//div[@role='dialog']//button[contains(.,'Oke')]");

    public AbsensiPage() {
        super();
    }

    // =========================================================================
    // BROWSER POPUP HANDLING
    // =========================================================================

    public void dismissPasswordPopupIfPresent() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(passwordPopup));
            logger.warn("Password change popup detected — dismissing...");
            click(passwordPopupOk);
            logger.info("Password popup dismissed");
        } catch (Exception e) {
            logger.debug("No password popup detected");
        }
    }

    // =========================================================================
    // CAMERA
    // =========================================================================

    public boolean isCameraPagePresent() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(cameraVideo));
            logger.info("Camera page detected (video element present in DOM)");
            return true;
        } catch (Exception e) {
            logger.info("Camera page NOT detected");
            return false;
        }
    }

    public boolean isCameraButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(cameraButton));
            return true;
        } catch (Exception e) {
            logger.warn("Camera button not visible");
            return false;
        }
    }

    public void clickCameraButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(cameraButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        try {
            btn.click();
            logger.info("Clicked camera button (regular click)");
        } catch (Exception e) {
            logger.warn("Regular click failed, trying JavaScript click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            logger.info("Clicked camera button (JavaScript click)");
        }
    }

    public void clickCloseCamera() {
        click(closeCameraButton);
    }

    // =========================================================================
    // ABSEN MASUK FORM
    // =========================================================================

    public boolean isAbsenMasukFormPresent() {
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(jamAbsenField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getJamAbsenValue() {
        waitForVisibility(jamAbsenField);
        WebElement el = driver.findElement(jamAbsenField);
        return el.getAttribute("value");
    }

    public void selectTipeAbsenWfh() {
        click(tipeAbsenDropdown);
        wait.until(ExpectedConditions.visibilityOfElementLocated(tipeAbsenListbox));
        wait.until(ExpectedConditions.elementToBeClickable(tipeAbsenOptionWfh)).click();
        logger.info("Selected Tipe Absen: Work From Home");
    }

    public void selectTipeAbsenWfo() {
        click(tipeAbsenDropdown);
        wait.until(ExpectedConditions.visibilityOfElementLocated(tipeAbsenListbox));
        wait.until(ExpectedConditions.elementToBeClickable(tipeAbsenOptionWfo)).click();
        logger.info("Selected Tipe Absen: Work From Office");
    }

    public String getSelectedTipeAbsen() {
        return getText(tipeAbsenDropdown);
    }

    public void enterNoteMasuk(String note) {
        typeText(noteFieldMasuk, note);
    }

    public void submitAbsenMasuk() {
        click(absenMasukSubmitButton);
        logger.info("Submitted Absen Masuk");
    }

    public boolean isAbsenMasukFormDisplayed() {
        return isDisplayed(jamAbsenField) && isDisplayed(absenMasukSubmitButton);
    }

    // =========================================================================
    // ABSEN PULANG FORM (Keluar flow)
    // =========================================================================

    public boolean isAbsenPulangFormPresent() {
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(timeOutField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTimeOutValue() {
        waitForVisibility(timeOutField);
        WebElement el = driver.findElement(timeOutField);
        return el.getAttribute("value");
    }

    public boolean isTimeOutDisabled() {
        WebElement el = driver.findElement(timeOutField);
        String disabled = el.getAttribute("disabled");
        return disabled != null;
    }

    public void enterKeluarNote(String note) {
        typeText(noteFieldPulang, note);
    }

    public void submitAbsenPulang() {
        click(absenPulangSubmitButton);
        logger.info("Submitted Absen Pulang");
    }

    public boolean isAbsenPulangFormDisplayed() {
        return isDisplayed(timeOutField) && isDisplayed(absenPulangSubmitButton);
    }

    // =========================================================================
    // HISTORY ABSENSI
    // =========================================================================

    public boolean isHistorySectionDisplayed() {
        return isDisplayed(historySection);
    }

    public int getHistoryEntryCount() {
        return findAll(historyEntries).size();
    }

    public HistoryEntry getLatestHistoryEntry() {
        List<WebElement> entries = findAll(historyEntries);
        logger.debug("Total history entries found: {}", entries.size());

        if (entries.isEmpty()) {
            return null;
        }

        int limit = Math.min(5, entries.size());
        for (int i = 0; i < limit; i++) {
            WebElement entry = entries.get(i);
            try {
                String name = entry.findElement(historyEntryName).getText();
                String date = entry.findElement(historyEntryDate).getText();
                String type = entry.findElement(historyEntryType).getText();
                logger.debug("History entry [{}]: name='{}', date='{}', type='{}'", i, name, date, type);
            } catch (Exception e) {
                logger.debug("History entry [{}]: could not read fields", i);
            }
        }

        WebElement latest = entries.get(0);
        return new HistoryEntry(
            latest.findElement(historyEntryName).getText(),
            latest.findElement(historyEntryDate).getText(),
            latest.findElement(historyEntryType).getText(),
            latest.findElement(historyEntryTime).getText()
        );
    }

    public boolean hasTodayEntryWithType(String expectedType) {
        String today = getTodayDateString();
        logger.debug("Looking for today's date: '{}', type: '{}'", today, expectedType);

        List<WebElement> entries = findAll(historyEntries);
        logger.debug("Total entries to search: {}", entries.size());

        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            WebElement entry = entries.get(i);
            String dateText = entry.findElement(historyEntryDate).getText();
            String typeText = entry.findElement(historyEntryType).getText();
            logger.debug("Checking entry [{}]: date='{}', type='{}'", i, dateText, typeText);

            if (dateText.contains(today) && typeText.equalsIgnoreCase(expectedType)) {
                logger.debug("MATCH FOUND at index {}", i);
                return true;
            }
        }
        logger.debug("No matching entry found for today");
        return false;
    }

    // =========================================================================
    // KELUAR BUTTON
    //
    // isKeluarButtonDisplayed() uses DOM presence + scrollIntoView instead of
    // visibilityOfElementLocated, because:
    //   1. The history section is below the fold — Selenium's visibility check
    //      returns false for off-screen elements even when they exist in DOM.
    //   2. We only need to know if the button exists, not whether it's in the
    //      current viewport.
    // =========================================================================

    public boolean isKeluarButtonDisplayed() {
        try {
            // Wait for the element to appear in DOM — history section loads async
            shortWait.until(ExpectedConditions.presenceOfElementLocated(keluarButton));
            WebElement el = driver.findElement(keluarButton);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el);
            return el.isDisplayed();
        } catch (Exception e) {
            logger.debug("Keluar button not found or not visible: {}", e.getMessage());
            return false;
        }
    }

    public void clickKeluar() {
        List<WebElement> elements = driver.findElements(keluarButton);
        if (elements.isEmpty()) {
            throw new RuntimeException("Keluar button not found in DOM");
        }
        WebElement el = elements.get(0);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", el);
        try {
            el.click();
            logger.info("Clicked Keluar button (regular click)");
        } catch (Exception e) {
            logger.warn("Regular click failed on Keluar, trying JavaScript click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            logger.info("Clicked Keluar button (JavaScript click)");
        }
    }

    public void waitForClockOutTimeToAppear() {
        wait.until(driver -> {
            List<WebElement> entries = driver.findElements(historyEntries);
            if (entries.isEmpty()) return false;
            try {
                String timeText = entries.get(0).findElement(historyEntryTime).getText();
                logger.debug("Waiting for clock-out time, current value: '{}'", timeText);
                return !timeText.endsWith("- -");
            } catch (Exception e) {
                return false;
            }
        });
    }

    // =========================================================================
    // PERMISSION DIALOG
    // =========================================================================

    public boolean isPermissionDialogDisplayed() {
        return isDisplayed(permissionDialog);
    }

    public void clickPermissionDialogOk() {
        click(permissionDialogOkButton);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private String getTodayDateString() {
        Locale indonesian = new Locale("id", "ID");
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ofPattern("d MMMM yyyy", indonesian));
    }

    // =========================================================================
    // INNER CLASS
    // =========================================================================

    public static class HistoryEntry {
        public final String name;
        public final String date;
        public final String type;
        public final String time;

        public HistoryEntry(String name, String date, String type, String time) {
            this.name = name;
            this.date = date;
            this.type = type;
            this.time = time;
        }

        @Override
        public String toString() {
            return String.format("HistoryEntry{name='%s', date='%s', type='%s', time='%s'}",
                name, date, type, time);
        }
    }
}
