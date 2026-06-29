package com.kelompok1.cucumber.stepdefinitions.web;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.kelompok1.cucumber.pages.web.LaporanCutiPage;

/**
 * Step Definitions for Laporan Cuti page.
 * URL: https://magang.dikahadir.com/laporan/cuti
 *
 * Background reuses @Given("web user is logged in") from WebLoginSteps.
 */
public class LaporanCutiSteps {

    private static final Logger logger = LoggerFactory.getLogger(LaporanCutiSteps.class);

    private LaporanCutiPage cutiPage() {
        return new LaporanCutiPage();
    }

    // =========================================================================
    // GIVEN
    // =========================================================================

    @Given("web user is on the laporan cuti page")
    public void webUserIsOnLaporanCutiPage() {
        cutiPage().navigateToPage();
        Assert.assertTrue(cutiPage().isOnPage(),
            "Should be on laporan cuti page — https://magang.dikahadir.com/laporan/cuti");
        logger.info("Navigated to laporan cuti page");
    }

    // =========================================================================
    // WHEN — Search
    // =========================================================================

    @When("admin searches cuti by employee name {string}")
    public void adminSearchesCutiByEmployeeName(String keyword) {
        logger.info("Typing employee name: '{}'", keyword);
        cutiPage().enterSearchKeyword(keyword);
    }

    @When("admin clicks the cuti search button")
    public void adminClicksCutiSearchButton() {
        cutiPage().clickSearch();
    }

    @When("admin clicks the cuti reset button")
    public void adminClicksCutiResetButton() {
        cutiPage().clickReset();
    }

    // =========================================================================
    // WHEN — Date Range
    //
    // Step accepts start and end independently so each can have its own
    // month and year — needed for cross-year ranges like Jan 2024 to Jan 2026.
    // Month values are 0-indexed to match react-date-range select option values
    // (January=0, February=1, ... December=11).
    // =========================================================================

    @When("admin selects cuti date range from day {int} month {int} year {int} to day {int} month {int} year {int}")
    public void adminSelectsCutiDateRange(int startDay, int startMonth, int startYear,
                                          int endDay,   int endMonth,   int endYear) {
        logger.info("Selecting date range: {}/{}/{} to {}/{}/{}",
            startDay, startMonth, startYear, endDay, endMonth, endYear);
        cutiPage().selectDateRange(startDay, startMonth, startYear, endDay, endMonth, endYear);
    }

    // =========================================================================
    // WHEN — Filter Dialog
    // =========================================================================

    @When("admin opens the cuti filter dialog")
    public void adminOpensCutiFilterDialog() {
        cutiPage().clickFilterButton();
    }

    @When("admin selects cuti unit {string} in the filter")
    public void adminSelectsCutiUnitInFilter(String unit) {
        logger.info("Selecting unit: '{}'", unit);
        cutiPage().selectUnit(unit);
    }

    @When("admin applies the cuti filter")
    public void adminAppliesCutiFilter() {
        cutiPage().clickApplyFilter();
    }

    // =========================================================================
    // THEN
    // =========================================================================

    @Then("the cuti table should display results")
    public void cutiTableShouldDisplayResults() {
        cutiPage().waitForTableToHaveResults();
        int actualRows = cutiPage().getTableRowCount();
        Assert.assertTrue(actualRows > 0,
            "Cuti table should have at least one row but found 0");
        logger.info("Table shows {} rows", actualRows);
    }

    @Then("the cuti search input should be empty")
    public void cutiSearchInputShouldBeEmpty() {
        String value = cutiPage().getSearchInputValue();
        Assert.assertTrue(cutiPage().isSearchInputEmpty(),
            "Search input should be empty after reset but contains: '" + value + "'");
        logger.info("Search input is empty after reset — confirmed");
    }
}
