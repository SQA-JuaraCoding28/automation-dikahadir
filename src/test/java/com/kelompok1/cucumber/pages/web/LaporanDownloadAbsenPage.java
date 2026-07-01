package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.kelompok1.cucumber.pages.BasePage;

/**
 * Page Object for the Download Absen report page.
 * URL: https://magang.dikahadir.com/laporan/generate-report-type
 */
public class LaporanDownloadAbsenPage extends BasePage {

    private static final String PAGE_URL = "https://magang.dikahadir.com/laporan/generate-report-type";

    // =========================================================================
    // SIDEBAR NAVIGATION
    // =========================================================================
    private final By laporanMenu = By.xpath("//*[contains(text(),'Laporan')]");
    private final By downloadAbsenSubmenu = By.xpath("//*[contains(text(),'Download Absen')]");

    // =========================================================================
    // DATE RANGE
    // =========================================================================
    private final By startDateInput = By.xpath("//input[@placeholder='Start Date']");
    private final By endDateInput   = By.xpath("//input[@placeholder='End Date']");

    private final By startDateCalendarBtn = By.xpath(
        "//input[@placeholder='Start Date']/following-sibling::div//button");
    private final By endDateCalendarBtn = By.xpath(
        "//input[@placeholder='End Date']/following-sibling::div//button");

    // Calendar popup (react-date-range)
    private final By calendarMonthSelect = By.cssSelector("span.rdrMonthPicker select");
    private final By calendarYearSelect  = By.cssSelector("span.rdrYearPicker select");

    // =========================================================================
    // FILTER INPUTS — MUI Autocomplete
    // =========================================================================
    private final By nikInput         = By.xpath("//input[@placeholder='Cari berdasarkan NIK']");
    private final By namaUserInput    = By.xpath("//input[@placeholder='Cari berdasarkan nama']");
    private final By namaUplinerInput = By.xpath("//input[@placeholder='Cari berdasarkan upliner']");
    private final By divisiInput      = By.xpath("//input[@placeholder='Pilih Divisi']");
    private final By unitInput        = By.xpath("//input[@placeholder='Pilih Unit']");
    private final By tipeReportInput  = By.xpath("//input[@placeholder='Pilih Tipe']");

    // =========================================================================
    // ACTION BUTTONS
    // =========================================================================
    private final By downloadButton = By.xpath("//button[@type='submit' and contains(.,'Download')]");
    private final By resetButton    = By.xpath("//button[@type='button' and contains(.,'Reset')]");

    // =========================================================================
    // ERROR / VALIDATION
    // =========================================================================
    private final By errorAlert = By.cssSelector(".MuiAlert-filledError, .MuiAlert-standardError");
    private final By inlineError  = By.cssSelector(".Mui-error, .MuiFormHelperText-root");

    public LaporanDownloadAbsenPage() {
        super();
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public void navigateViaSidebar() {
        try {
            if (!driver.findElements(downloadAbsenSubmenu).isEmpty()
                    && driver.findElement(downloadAbsenSubmenu).isDisplayed()) {
                click(downloadAbsenSubmenu);
                logger.info("Clicked Download Absen submenu directly");
                return;
            }
        } catch (Exception e) {
            logger.debug("Download Absen submenu not visible yet, expanding Laporan menu");
        }

        click(laporanMenu);
        logger.info("Expanded Laporan sidebar menu");
        wait.until(ExpectedConditions.elementToBeClickable(downloadAbsenSubmenu)).click();
        logger.info("Clicked Download Absen submenu");
    }

    public boolean isOnPage() {
        return waitForUrlContains("/laporan/generate-report-type");
    }

    // =========================================================================
    // DATE RANGE PICKER
    // =========================================================================

    public void selectDateRange(int startDay, int startMonth, int startYear,
                                int endDay, int endMonth, int endYear) {
        click(startDateCalendarBtn);
        logger.info("Opened date range calendar");

        wait.until(ExpectedConditions.visibilityOfElementLocated(calendarMonthSelect));

        // Start date
        new Select(driver.findElement(calendarMonthSelect)).selectByValue(String.valueOf(startMonth));
        new Select(driver.findElement(calendarYearSelect)).selectByValue(String.valueOf(startYear));
        logger.info("Navigated to start month={} year={}", startMonth, startYear);
        clickCalendarDay(startDay);
        logger.info("Clicked start day: {}", startDay);

        // End date
        new Select(driver.findElement(calendarMonthSelect)).selectByValue(String.valueOf(endMonth));
        new Select(driver.findElement(calendarYearSelect)).selectByValue(String.valueOf(endYear));
        logger.info("Navigated to end month={} year={}", endMonth, endYear);
        clickCalendarDay(endDay);
        logger.info("Clicked end day: {}", endDay);

        // Close calendar by clicking the calendar button again (no save button on this page)
        click(startDateCalendarBtn);
        logger.info("Clicked calendar button to close date picker");
    }

    private void clickCalendarDay(int day) {
        By dayLocator = By.xpath(
            "//button[contains(@class,'rdrDay') and not(contains(@class,'rdrDayPassive'))]" +
            "//span[@class='rdrDayNumber']/span[text()='" + day + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
    }

    // =========================================================================
    // AUTOCOMPLETE FILTERS
    // =========================================================================

    public void selectNik(String nik) {
        selectAutocomplete(nikInput, nik, false);
    }

    public void selectNamaUser(String nama) {
        selectAutocomplete(namaUserInput, nama, false);
    }

    public void selectNamaUpliner(String nama) {
        selectAutocomplete(namaUplinerInput, nama, false);
    }

    public void selectDivisi(String divisi) {
        selectAutocomplete(divisiInput, divisi, true); // ← delay enabled
    }

    public void selectUnit(String unit) {
        selectAutocomplete(unitInput, unit, false);
    }

    /**
     * Tipe Report is already pre-filled — we open the dropdown by clicking the
     * arrow and select the option directly without typing.
     */
    public void selectTipeReport(String tipe) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(tipeReportInput));
        WebElement arrow = input.findElement(By.xpath(
            "following::button[contains(@class,'MuiAutocomplete-popupIndicator')][1]"));
        arrow.click();
        logger.info("Opened tipe report dropdown");

        By option = By.xpath("//li[@role='option' and contains(.,'" + tipe + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
        logger.info("Selected tipe report: '{}'", tipe);
    }

    /**
     * Robust MUI Autocomplete selection.
     *
     * @param inputLocator the input field locator
     * @param value        the text to type
     * @param useDelay     if true, types character-by-character with 50ms delay
     *                     (needed for Divisi which needs debounce time to show dropdown)
     */
    private void selectAutocomplete(By inputLocator, String value, boolean useDelay) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        input.click();

        // Hard clear
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);

        // Click dropdown arrow to force-open the list first
        try {
            WebElement arrow = input.findElement(By.xpath(
                "following::button[contains(@class,'MuiAutocomplete-popupIndicator')][1]"));
            arrow.click();
            logger.info("Clicked dropdown arrow for: '{}'", value);
        } catch (Exception e) {
            logger.warn("Could not click dropdown arrow, continuing with typing only");
        }

        // Type value — with optional delay for Divisi
        if (useDelay) {
            for (char c : value.toCharArray()) {
                input.sendKeys(String.valueOf(c));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
            logger.info("Typed into autocomplete (with 50ms delay): '{}'", value);
        } else {
            input.sendKeys(value);
            logger.info("Typed into autocomplete: '{}'", value);
        }

        By option = By.xpath("//li[@role='option' and contains(.,'" + value + "')]");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
            logger.info("Selected autocomplete option: '{}'", value);
        } catch (Exception firstTry) {
            // Fallback: backspace last char and retype to trigger dropdown
            logger.warn("Dropdown not showing '{}', applying backspace+retype workaround", value);
            input.sendKeys(Keys.BACK_SPACE);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            input.sendKeys(value.substring(value.length() - 1));
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
            logger.info("Selected autocomplete option on retry: '{}'", value);
        }
    }

    // =========================================================================
    // ACTION BUTTONS
    // =========================================================================

    public void clickDownload() {
        click(downloadButton);
        logger.info("Clicked Download button");
    }

    public void clickReset() {
        click(resetButton);
        logger.info("Clicked Reset button");
        // Wait for React to re-render the cleared form
        wait.until(ExpectedConditions.presenceOfElementLocated(startDateInput));
    }

    // =========================================================================
    // STATE / VALIDATION
    // =========================================================================

    public boolean isErrorDisplayed() {
        return isDisplayed(errorAlert) || isDisplayed(inlineError);
    }

    public String getErrorMessage() {
        if (isDisplayed(errorAlert)) {
            return getText(errorAlert);
        }
        if (isDisplayed(inlineError)) {
            return getText(inlineError);
        }
        return "";
    }

    public boolean areFiltersCleared() {
        // Use wait + retry to survive React DOM refresh after Reset
        String startDate = getInputValueSafe(startDateInput);
        String endDate   = getInputValueSafe(endDateInput);
        String nik       = getInputValueSafe(nikInput);

        boolean cleared = (startDate == null || startDate.isEmpty())
                       && (endDate == null || endDate.isEmpty())
                       && (nik == null || nik.isEmpty());

        logger.info("Filters cleared check: startDate='{}', endDate='{}', nik='{}' — cleared={}",
            startDate, endDate, nik, cleared);
        return cleared;
    }

    /**
     * Safely reads an input value, re-trying on StaleElementReference.
     */
    private String getInputValueSafe(By locator) {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                return el.getAttribute("value");
            } catch (StaleElementReferenceException e) {
                logger.warn("Stale element reading {}, retrying {}/3", locator, i + 1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }
        return null;
    }
}
