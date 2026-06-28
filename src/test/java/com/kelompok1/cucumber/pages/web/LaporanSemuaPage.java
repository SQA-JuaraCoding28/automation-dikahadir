package com.kelompok1.cucumber.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.kelompok1.cucumber.pages.BasePage;

import java.util.List;

/**
 * Page Object for the Laporan Semua page.
 * URL: https://magang.dikahadir.com/laporan/all
 */
public class LaporanSemuaPage extends BasePage {

    private static final String PAGE_URL = "https://magang.dikahadir.com/laporan/all";

    // =========================================================================
    // SEARCH BAR
    // =========================================================================
    private final By searchInput = By.id("search");

    private final By searchButton = By.xpath(
        "//button[contains(.,'Search') and @type='submit']");

    private final By resetButton = By.xpath(
        "//button[contains(.,'Reset')]");

    // =========================================================================
    // DATE RANGE PICKER
    // =========================================================================
    private final By startDateCalendarIcon = By.xpath(
        "//input[@placeholder='Start Date']/following-sibling::div//button");

    private final By calendarMonthSelect = By.cssSelector("span.rdrMonthPicker select");
    private final By calendarYearSelect  = By.cssSelector("span.rdrYearPicker select");
    private final By calendarSaveButton  = By.xpath("//button[normalize-space(text())='save']");

    // =========================================================================
    // FILTER DIALOG
    // =========================================================================
    private final By filterButton = By.xpath(
        "//button[.//*[contains(@class,'feather-filter')]]");

    private final By unitAutocompleteInput = By.id("job_departement");

    private final By filterApplyButton = By.xpath(
        "//button[@type='submit' and contains(.,'Terapkan')]");

    // =========================================================================
    // TABLE
    //
    // Uses MuiTableRow-hover which is the stable MUI class present on all
    // data rows — confirmed from the actual rendered HTML the user provided.
    // =========================================================================
    private final By tableRows = By.cssSelector("tr.MuiTableRow-hover");

    public LaporanSemuaPage() {
        super();
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public LaporanSemuaPage navigateToPage() {
        navigateTo(PAGE_URL);
        return this;
    }

    public boolean isOnPage() {
        return waitForUrlContains("/laporan/all");
    }

    // =========================================================================
    // SEARCH
    // =========================================================================

    public void enterSearchKeyword(String keyword) {
        typeText(searchInput, keyword);
        logger.info("Entered search keyword: '{}'", keyword);
    }

    public void clickSearch() {
        click(searchButton);
        logger.info("Clicked Search button");
    }

    public void clickReset() {
        click(resetButton);
        logger.info("Clicked Reset button");
    }

    public String getSearchInputValue() {
        return driver.findElement(searchInput).getAttribute("value");
    }

    public boolean isSearchInputEmpty() {
        return getSearchInputValue().isEmpty();
    }

    // =========================================================================
    // DATE RANGE PICKER
    // =========================================================================

    /**
     * Selects a date range using the react-date-range calendar picker.
     *
     * @param startDay day number for start date (1-31)
     * @param endDay   day number for end date (1-31)
     * @param month    0-indexed month value matching the <select> option values
     *                 (e.g. January=0, June=5, December=11)
     * @param year     4-digit year
     */
    public void selectDateRange(int startDay, int endDay, int month, int year) {
        click(startDateCalendarIcon);
        logger.info("Opened date range calendar");

        wait.until(ExpectedConditions.visibilityOfElementLocated(calendarMonthSelect));

        new Select(driver.findElement(calendarMonthSelect))
            .selectByValue(String.valueOf(month));
        new Select(driver.findElement(calendarYearSelect))
            .selectByValue(String.valueOf(year));
        logger.info("Navigated calendar to month={} year={}", month, year);

        clickCalendarDay(startDay);
        logger.info("Selected start day: {}", startDay);

        clickCalendarDay(endDay);
        logger.info("Selected end day: {}", endDay);

        click(calendarSaveButton);
        logger.info("Clicked calendar save");
    }

    /**
     * Clicks a non-passive day in the react-date-range calendar.
     * Passive days (greyed-out days from prev/next month) are excluded.
     */
    private void clickCalendarDay(int day) {
        By dayLocator = By.xpath(
            "//button[contains(@class,'rdrDay') and not(contains(@class,'rdrDayPassive'))]" +
            "//span[@class='rdrDayNumber']/span[text()='" + day + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
    }

    // =========================================================================
    // FILTER DIALOG
    // =========================================================================

    public void clickFilterButton() {
        click(filterButton);
        logger.info("Opened filter dialog");
    }

    public void selectUnit(String unit) {
        WebElement input = wait.until(
            ExpectedConditions.visibilityOfElementLocated(unitAutocompleteInput));
        input.clear();
        input.sendKeys(unit);
        logger.info("Typed unit: '{}'", unit);

        By unitOption = By.xpath("//li[@role='option' and contains(.,'" + unit + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(unitOption)).click();
        logger.info("Selected unit option: '{}'", unit);
    }

    public void clickApplyFilter() {
        click(filterApplyButton);
        logger.info("Clicked Terapkan");
    }

    // =========================================================================
    // TABLE
    // =========================================================================

    public int getTableRowCount() {
        List<WebElement> rows = findAll(tableRows);
        logger.debug("Current table row count: {}", rows.size());
        return rows.size();
    }

    /**
     * Waits until at least one row appears in the table.
     * Throws AssertionError with the current URL if it times out,
     * so you can tell whether the page loaded at all.
     */
    public void waitForTableToHaveResults() {
        try {
            wait.until(driver -> !driver.findElements(tableRows).isEmpty());
        } catch (Exception e) {
            throw new AssertionError(
                "Table still shows 0 rows after 15s. " +
                "Search or filter may not have been applied. " +
                "Current URL: " + driver.getCurrentUrl());
        }
    }
}
